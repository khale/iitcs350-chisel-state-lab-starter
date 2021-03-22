use std::error::Error;
use std::io::prelude::*;
use std::fs;
use regex::Regex;
use packed_struct::prelude::*;


#[derive(PrimitiveEnum_u8, Clone, Copy, Debug, PartialEq)]
pub enum OpType {
    OpAdd = 0, 
    OpAnd = 1,
    OpOr  = 2,
    OpXor = 3,
    None  = 5,
}


#[derive(PackedStruct)]
#[packed_struct(bit_numbering="msb0")]
pub struct Instr {
    #[packed_field(bits="0..=2", ty="enum")]
    op: OpType,
    #[packed_field(bits="3..=10")]
    opr1: Integer<u8, packed_bits::Bits8>,
    #[packed_field(bits="11..=18")]
    opr2: Integer<u8, packed_bits::Bits8>,
    #[packed_field(bits="19..=26")]
    rsvd1: Integer<u8, packed_bits::Bits8>,
    #[packed_field(bits="27..=31")]
    rsvd2: Integer<u8, packed_bits::Bits5>,
}

pub fn run (fname: &str, output: &str) -> Result<(), Box<dyn Error>> {

    let contents = fs::read_to_string(fname)?;

    let mut outfile = fs::File::create(output)?;

    let re = Regex::new(r"([[:word:]]+)\s+#(\d+)\s*,\s*#(\d+)\s*").unwrap();

    for line in contents.lines() {
        for cap in re.captures_iter(line) {
            let opr1 = cap.get(2).map_or("", |m| m.as_str()).parse::<u8>().unwrap();
            let opr2 = cap.get(3).map_or("", |m| m.as_str()).parse::<u8>().unwrap();
            let op = match &cap[1] {
                "ADD" => OpType::OpAdd,
                "AND" => OpType::OpAnd,
                "OR"  => OpType::OpOr,
                "XOR" => OpType::OpXor,
                v =>  {
                    panic!("Unrecognized mnemonic ({}) encountered", v);
                },
            };

            let instr = Instr {
                op: op,
                opr1: opr1.into(),
                opr2: opr2.into(),
                rsvd1: 0xffu8.into(), // ignored
                rsvd2: 0xffu8.into(), // ignored
            };

            let y: [u8; 4] = instr.pack()?;
            outfile.write_all(&y)?;
            outfile.flush()?;
        }
    }

    Ok(())
}
