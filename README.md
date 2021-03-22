# IIT CS 350 Lab 5: Sequential Logic in Chisel

This is a starter repo that contains three designs that you will complete:

## Shift Register
You will implement a basic unidirectional shift register that
accepts an input bit from the right and left shifts the value
every clock cycle.

## FSM
You will implement a finite state machine that
accepts all binary inputs that contain the pattern `1010`

## ALU Sequencer Machine
You'll implement a basic (non-Turing complete) computer
that only incorporates "operate" instructions. All instructions
are 32 bits long (4 bytes) and operate directly on 8-bit immediate (literal)
operands. You will have to figure out how to fetch instructions 
from a memory we provide, decode them, send the operands to an ALU for
execution, and output the result.

## Testing
To test the shift register, run
```
[you@vagrant] make test-sr
```

To test the finite state machine, run

```
[you@vagrant] make test-fsm
```

To test your ALU for the ALU sequencer, run
```
[you@vagrant] make test-alu
```

To test the entire sequencer, run 
```
[you@vagrant] make test-emu
```

