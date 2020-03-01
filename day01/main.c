#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "../utils.h"

int fuel_needed(int mass) {
  return max(mass / 3 - 2, 0);
}

int fuel_needed_rec(int mass) {
  int sum = 0, curr = mass;

  while((curr = fuel_needed(curr)) > 0) sum += curr;

  return sum;
}


int main(void) {
  char *tofree;
  char *filename = "input";

  read_file(filename, &tofree);
  char *input = trim(tofree);

  char *token;
  int sum1 = 0, sum2 = 0;

  while((token = strsep(&input, "\n"))) {
    int mass = (int)strtol(token, (char**)NULL, 10);

    sum1 += fuel_needed(mass);
    sum2 += fuel_needed_rec(mass);
  }

  printf("answer1: %d\n", sum1);
  printf("answer2: %d\n", sum2);

  free(tofree);

  return 0;
}
