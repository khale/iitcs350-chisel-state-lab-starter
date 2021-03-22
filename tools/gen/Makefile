

RUST_SRC:=$(shell find src -name "*.rs")

all: gen

gen: $(RUST_SRC)
	cargo build --release

