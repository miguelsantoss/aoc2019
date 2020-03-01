#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "../utils.h"

int* clone(int*, int);
int mode(int, int);
int arg(int*, int, int);

void execute(int** ops, int** in, int** out) {
  int *ins = *ops, *in_c = *in, *out_c = *out;
  int ip = 0, curr, inc = 0;

  while((curr = ins[ip] % 100)) {
    switch(curr) {
      // ADD
      case 1: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        int res = ins[ip+3];
        ins[res] = ins[arg1] + ins[arg2];
        inc = 4;
        break;
      }
      // MUL
      case 2: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        int res = ins[ip+3];
        ins[res] = ins[arg1] * ins[arg2];
        inc = 4;
        break;
      }
      // TAKE INPUT
      case 3: {
        int argg = arg(ins, ip, 1);
        ins[argg] = *in_c++;
        inc = 2;
        break;
      }
      // PUT OUTPUT
      case 4: {
        int argg = arg(ins, ip, 1);
        *out_c++ = ins[argg];
        inc = 2;
        break;
      }
      // JUMP IF TRUE
      case 5: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        inc = 3;
        if (ins[arg1] != 0) {
          ip = ins[arg2];
          inc = 0;
        }
        break;
      }
      // JUMP IF FALSE
      case 6: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        inc = 3;
        if (ins[arg1] == 0) {
          ip = ins[arg2];
          inc = 0;
        }
        break;
      }
      // LESS THAN
      case 7: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        int res = ins[ip+3];
        ins[res] = ins[arg1] < ins[arg2];
        inc = 4;
        break;
      }
      // EQUALS
      case 8: {
        int arg1 = arg(ins, ip, 1);
        int arg2 = arg(ins, ip, 2);
        int res = ins[ip+3];
        ins[res] = ins[arg1] == ins[arg2];
        inc = 4;
        break;
      }
      case 99: return;
    }
    ip += inc;
  }
}

int main(void) {
  char *tofree;
  char *filename = "input";

  read_file(filename, &tofree);
  char *input = trim(tofree);

  int size = count_delimiters(input, ",") + 1;
  int *ins = (int*)malloc(sizeof(int) * size);

  char *token;
  for(int i = 0; i < size; i++) {
    token = strsep(&input, ",");
    ins[i] = (int)strtol(token, (char**)NULL, 10);
  }

  int *a1 = clone(ins, size);

  int memitems = 10;
  int memsize = sizeof(int) * memitems;
  int *in = (int*)malloc(memsize);
  int *out = (int*)malloc(memsize);
  memset(in, 0, memsize);
  memset(out, 0, memsize);

  in[0] = 1;
  execute(&a1, &in, &out);

  for(int i = 0; i < memitems; i++) printf("%d ", out[i]);
  printf("\n");

  memset(in, 0, memsize);
  memset(out, 0, memsize);
  int *a2 = clone(ins, size);

  in[0] = 5;
  execute(&a2, &in, &out);

  for(int i = 0; i < memitems; i++) printf("%d ", out[i]);
  printf("\n");

  free(a1); free(a2); free(tofree);
  free(ins); free(in); free(out);

  return 0;
}

int* clone(int* src, int size) {
  int *p = (int*)malloc(sizeof(int) * size);
  memcpy(p, src, sizeof(int) * size);
  return p;
}

int mode(int ins, int pos) {
  int i = 1;
  int m = ins / 100;

  while((i++ < pos)) m /= 10;
  m = m % 10;

  assert(m == 0 || m == 1);

  return m;
}

int arg(int *ins, int ip, int pos) {
  int curr = ins[ip];
  int ret = ip + pos;
  int m = mode(curr, pos);

  if (m == 0)
    ret = ins[ret];

  return ret;
}

