#ifndef __COMMON_H__
#include <stdint.h>
#include <stdio.h>
#include <assert.h>

#define EMU_DEBUG 0

#if EMU_DEBUG == 1
#define DEBUG_PRINT(fmt, args...) printf("DEBUG: " fmt, ##args)
#else
#define DEBUG_PRINT(fmt, args...)
#endif

typedef uint8_t paddr_t;
typedef uint32_t word_t;

extern void init_ram(const char *img);

#endif
