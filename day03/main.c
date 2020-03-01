#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../utils.h"

struct arr {
  struct point *items;
  int size;
};

struct point {
  int x;
  int y;
};

int main(void) {
  char *tofree;
  char *filename = "input";

  read_file(filename, &tofree);
  char *input = trim(tofree);

  char *token_line, *token;
  int n_wires = count_delimiters(input, "\n") + 1, i = 0;

  struct arr *wires = (struct arr*)malloc(sizeof(struct arr) * n_wires);

  while((token_line = (char*)strsep(&input, "\n")) != NULL) {
    int x = 0, y = 0;

    wires[i].size = count_delimiters(token_line, ",") - 1;
    wires[i].items = (struct point*)malloc(sizeof(struct point) * wires[i].size);

    int j = 0;
    while((token = (char*)strsep(&token_line, ",")) != NULL) {
      int steps = (int)strtol(token + 1, (char**)NULL, 10);

      switch(token[0]) {
        case('U'): {
          y += steps;
          break;
        }
        case('D'): {
          y -= steps;
          break;
        }
        case('R'): {
          x += steps;
          break;
        }
        case('L'): {
          x -= steps;
          break;
        }
        default: {
          break;
        }
      }

      wires[i].items[j].x = x;
      wires[i].items[j].y = y;

      j++;
    }

    i++;
  }

  free(tofree);

  return 0;
}
