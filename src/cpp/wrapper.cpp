#include <verilated.h>
#include <verilated_vcd_c.h>
#include <iostream>
#include <fstream>
#include <cstring>
#include <unistd.h>
#include <getopt.h>
#include "VTop.h"
#include "common.h"

#define MAX_IMAGE_NAME_LEN 256

using namespace std;

VTop* top;
VerilatedVcdC* tfp;

vluint64_t main_time      = 0;
const vluint64_t sim_time = -1;

static char image[MAX_IMAGE_NAME_LEN] = {"image.bin"};
static char trace_file[MAX_IMAGE_NAME_LEN] = {"build/emu.vcd"};
static bool trace_en = false;

double sc_time_stamp() {
    return main_time;
}


static void usage (const char * prog) {
    printf("%s [-i <input image>] [-t]\n"
            "   -i <input image> : specifies program binary to run (default image.bin)\n"
            "   -t [trace file]  : specifies to generate a waveform (.vcd) file\n",
            prog);
    exit(EXIT_SUCCESS);
}

static inline void parse_args(int argc, char *argv[]) {
    int arg;
    while ((arg = getopt(argc, argv, "i:th")) != -1) {
        switch (arg) {
            case 'i':
                memset(image, 0, MAX_IMAGE_NAME_LEN);
                strncpy(image, optarg, MAX_IMAGE_NAME_LEN);
                break;
            case 't':
                trace_en = true;
                if (optarg) {
                    memset(trace_file, 0, MAX_IMAGE_NAME_LEN);
                    strncpy(trace_file, optarg, MAX_IMAGE_NAME_LEN);
                }
                break;
            case 'h':
                usage(argv[0]);
                break;
            case '?':
                usage(argv[0]);
                break;
            default:
                fprintf(stderr, "Unknown command-line option\n");
                usage(argv[0]);
                break;
        }
    }
}

int main (int argc, char **argv)
{
    parse_args(argc, argv);
    Verilated::commandArgs(argc, argv);
    Verilated::traceEverOn(true);

    uint64_t ticks = 0;

    top = new VTop;
    tfp = new VerilatedVcdC;

    if (trace_en) {
        top->trace(tfp, 99);
        tfp->open("./build/emu.vcd");
    }

    init_ram(image);

    while (!Verilated::gotFinish() && (sim_time == 0 || main_time < sim_time))
    {
        if ((main_time % 10) == 1) {
            top->clock = 1;       // Toggle clock
            ticks++;
        }
        if ((main_time % 10) == 6) {
            top->clock = 0;
        }

        top->eval();

        if ((main_time % 10) == 1) {
            printf("Ticks: %lu Out: %01x\n", ticks, top->io_out);
        }
        tfp->dump(main_time);
        main_time++;

        if (top->io_addr == 0x3f)
            break;
    }
    
    tfp->close();
    top->final();
    delete top;
    delete tfp;
    return 0;
}
