all: test

TOP:=Top
BUILD:=./build
TOP_VLOG:=$(BUILD)/$(TOP).v

MILL:=mill

SEQCHISEL:=$(shell find src/main/scala/alu-sequencer/*.scala)

$(TOP_VLOG): $(SEQCHISEL)
	@mkdir -p $(@D)
	@$(MILL) chisel_statelab.run aluseq.Top.SimMain -td $(@D) --output-file $(@F)

SIM_TOP = $(TOP)

verilog: $(TOP_VLOG)


EMU_CSRC_DIR:=$(abspath ./src/cpp)
EMU_VSRC_DIR:=$(abspath ./src/v)
EMU_MKFILE:=$(BUILD)/emu-compile/V$(SIM_TOP).mk
EMU_CXXFILES:=$(shell find $(EMU_CSRC_DIR) -name "*.cpp")
EMU_VFILES:=$(shell find $(EMU_VSRC_DIR) -name "*.v")
EMU_DEPS:= $(EMU_VFILES) $(EMU_CXXFILES)
EMU_CXXFLAGS = -O3
EMU_LDFLAGS = -lpthread
EMU := $(BUILD)/emu

ASM_BIN_DIR:=binaries
ASM_SRC_DIR:=$(abspath ./test_asm)
ASM_SRC_FILES:=$(shell find $(ASM_SRC_DIR) -name "*.asm")
ASM_OBJ_FILES:=$(addprefix $(ASM_BIN_DIR)/, $(notdir $(ASM_SRC_FILES:%.asm=%.bin)))

VERILATOR_FLAGS = --top-module $(SIM_TOP) \
	-I$(abspath $(BUILD_DIR)) \
	-CFLAGS "$(EMU_CXXFLAGS)" \
	-LDFLAGS "$(EMU_LDFLAGS)" \
	-Wno-WIDTH\
	--trace

$(EMU_MKFILE): $(TOP_VLOG) 
	@echo "Building simulator config from Chisel output..."
	@mkdir -p $(@D)
	@verilator --cc --exe $(VERILATOR_FLAGS) \
		-o $(abspath $(EMU)) -Mdir $(@D) $^ $(EMU_DEPS)

$(EMU): $(EMU_MKFILE) $(EMU_DEPS)
	@echo "Building simulator..."
	@$(MAKE) -C $(dir $(EMU_MKFILE)) -f $(abspath $(EMU_MKFILE))

emu: $(EMU)

ASSEMBLER:=tools/gen/target/release/gen

$(ASSEMBLER): tools/gen/src/*.rs
	@echo "Building assembler..."
	@$(MAKE) -C tools/gen 

$(ASM_OBJ_FILES): $(ASSEMBLER) $(ASM_SRC_FILES)

$(ASM_BIN_DIR)/%.bin: $(ASM_SRC_DIR)/%.asm
	@echo "Compiling test binaries..."
	@$(ASSEMBLER) -f $< -o $@


test-all: test-sr test-fsm test-alu test-emu 

test-emu: $(EMU) $(ASM_OBJ_FILES) $(ASSEMBLER) src/main/scala/alu-sequencer/ALU.scala src/main/scala/alu-sequencer/Top.scala

test-alu: src/main/scala/alu-sequencer/ALU.scala
	@sbt 'testOnly aluseq.ALUTester -- -DwriteVcd=1'

test-fsm: src/main/scala/fsm/FSM.scala
	@sbt 'testOnly fsm.FSMTester -- -DwriteVcd=1'

# tests shift register code
test-sr: src/main/scala/shiftreg/ShiftReg.scala
	@sbt 'testOnly shiftreg.ShiftRegTester -- -DwriteVcd=1'

clean:
	@rm -rf ./build binaries/* out

dist-clean: clean
	@rm -rf tools/gen/target
