use clap::{Arg, App};

fn main() {
    let matches = App::new("Mini Assembler")
        .version("0.0.1")
        .author("Kyle Hale <khale@cs.iit.edu>")
        .about("Assembles a mini calculator language into binaries")
        .arg(Arg::with_name("asmfile")
             .short("f")
             .long("input")
             .required(true)
             .takes_value(true)
             .help("The assembly file to assemble"))
        .arg(Arg::with_name("outfile")
             .short("o")
             .long("output")
             .required(true)
             .takes_value(true)
             .help("The binary file to output to"))
        .get_matches();

    let asmfile = matches.value_of("asmfile").unwrap();
    let output  = matches.value_of("outfile").unwrap();
    gen::run(&asmfile, &output).expect("Something went wrong");
}
