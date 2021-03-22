#include "common.h"
#include <arpa/inet.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <stdio.h>

#define RAMSIZE 64

static word_t ram[RAMSIZE];

static inline void display_ram (void) {
    for (int i = 0; i < RAMSIZE; i++) {
        printf("%02x: %08x\n", i, ram[i]);
    }
}

static void load_image (const char *img) {
    int ret;
    struct stat st;
    size_t size = 0;

    stat(img, &st);

    if (st.st_size > (RAMSIZE*sizeof(word_t))) {
        size = RAMSIZE*sizeof(word_t);
    } else {
        size = st.st_size;
    }

    DEBUG_PRINT("Using size %ld B\n", size);

    FILE *fp = fopen(img, "rb");
    if (fp == NULL) {
        fprintf(stderr, "Could not open file '%s'\n", img);
        exit(EXIT_FAILURE);
    }

    ret = fread(ram, 1, size, fp);
    assert(ret == size);

    DEBUG_PRINT("Image size = %ld B\n", size);
    fclose(fp);

#if EMU_DEBUG==1
    display_ram();
#endif
}

void init_ram (const char *img) {
    DEBUG_PRINT("Image file: %s\n", img);

    load_image(img);

#if 1
    for(int i = 0; i < RAMSIZE; i++) {
        ram[i] = htobe32(ram[i]);
    }
#endif
}

extern "C" void ram_helper (paddr_t rIdx, 
                            word_t *rdata, 
                            paddr_t wIdx, 
                            word_t wdata, 
                            /*paddr_t wmask,*/ 
                            uint8_t wen) {
    int rIdxReg = rIdx;
    *rdata = ram[rIdxReg];
    if (wen) {
        ram[wIdx] = wdata;
    }
    DEBUG_PRINT("RAM access:\n"
            "\trIdx=%4x\n" 
            "\t*rdata=%4x\n"
            "\twIdx=%4x\n"
            "\t*wdata=%4x\n"
            "\twen=%x\n", 
            rIdx,  
            *rdata, 
            wIdx, 
            wdata, 
            wen);
}
