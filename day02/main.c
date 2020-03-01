#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../utils.h"

int* clone(int* src, int size) {
  int *p = (int*)malloc(sizeof(int) * size);
  memcpy(p, src, sizeof(int) * size);
  return p;
}

void execute(int** instructions) {
  int *ins = *instructions;
  int i = 0, curr;

  while((curr = ins[i]) != 99) {
    switch(curr) {
      case 1: {
        int arg1 = ins[ins[i + 1]];
        int arg2 = ins[ins[i + 2]];
        int res = ins[i + 3];
        ins[res] = arg1 + arg2;
        break;
      }
      case 2: {
        int arg1 = ins[ins[i + 1]];
        int arg2 = ins[ins[i + 2]];
        int res = ins[i + 3];
        ins[res] = arg1 * arg2;
        break;
      }
    }

    i += 4;
  }
}

int main(void) {
  char *tofree;
  char *filename = "input";

  read_file(filename, &tofree);
  char *input = trim(tofree);

  int size = count_delimiters(input, ",") - 1;
  int *ins = (int*)malloc(sizeof(int) * size);

  char *token;
  for(int i = 0; i < size; i++) {
    token = strsep(&input, ",");
    ins[i] = (int)strtol(token, (char**)NULL, 10);
  }

  int *a1 = clone(ins, size);
  a1[1] = 12;
  a1[2] = 2;

  execute(&a1);
  printf("answer1: %d\n", a1[0]);

  int target = 19690720;
  for(int n = 0; n < 100; n++) {
    for(int v = 0; v < 100; v++) {
      int *p = clone(ins, size);

      p[1] = n;
      p[2] = v;
      execute(&p);

      if (p[0] == target) {
        printf("answer2: %d\n", 100 * n + v);
        goto end;
      }

      free(p);
    }
  }

  end:
  free(tofree);
  free(ins);

  return 0;
}
