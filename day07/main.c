#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include "../utils.h"

void print(int a) {
  printf("%d\n", a);
}

struct arr {
  int* items;
  int size;
};

struct machine {
  struct arr ins;
  int* in;
  int* out;
};


struct machine create_machine(struct arr*);
void destroy_machine(struct machine*);

int* clone(int*, int);
int* create_arr(int);

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

  struct arr ins_arr;
  ins_arr.size = count_delimiters(input, ",") + 1;
  ins_arr.items = (int*)malloc(sizeof(int) * ins_arr.size);

  char *token;
  for(int i = 0; i < ins_arr.size; i++) {
    token = strsep(&input, ",");
    ins_arr.items[i] = (int)strtol(token, (char**)NULL, 10);
  }
  free(tofree);

  int memitems = 10;
  int memsize = sizeof(int)*memitems;

  struct machine m1 = create_machine(&ins_arr);
  struct machine m2 = create_machine(&ins_arr);
  struct machine m3 = create_machine(&ins_arr);
  struct machine m4 = create_machine(&ins_arr);
  struct machine m5 = create_machine(&ins_arr);

  free(ins_arr.items);

  m1.in = create_arr(memsize);
  m1.out = create_arr(memsize);

  m1.in[0] = 1;

  execute(&m1.ins.items, &m1.in, &m1.out);

  for(int i = 0; i < memitems; i++) printf("%d ", m1.out[i]);
  printf("\n");

  destroy_machine(&m1);

  return 0;
}

struct machine create_machine(struct arr* ins) {
  int *a1 = clone(ins->items, ins->size);

  struct arr new_ins = {.size = ins->size, .items = a1};
  struct machine m = {.ins = new_ins};

  return m;
}

void destroy_machine(struct machine* m) {
  free(m->in);
  free(m->out);
  free(m->ins.items);
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

int* create_arr(int size) {
  int *arr = (int*)malloc(size);
  memset(arr, 0, size);

  return arr;
}

