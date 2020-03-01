#include <stdio.h>
#include <stdlib.h>
#include "../utils.h"

int adjacent_digits(int num) {
  int n = num % 10;

  while((num = num / 10) > 0) {
    if (n == num % 10) return 1;
    n = num % 10;
  }

  return 0;
}

int adjacent_digits_only(int num) {
  int n = num % 10;
  int oc[10] = {0};

  while((num = num / 10) > 0) {
    if (n == num % 10) {
      oc[n]++;
    }
    n = num % 10;
  }

  for(int i = 0; i < 10; i++) {
    if(oc[i] == 1) return 1;
  }

  return 0;
}

int never_decrease(int num) {
  int n = num % 10;

  while((num = num / 10) > 0) {
    if (n < num % 10) return 0;
    n = num % 10;
  }

  return 1;
}

int main(void) {
  // puzzle input
  int min = 278384;
  int max = 824795;

  int a1 = 0, a2 = 0;

  for(int i = min; i < max; i++) {
    if(never_decrease(i))  {
      if(adjacent_digits(i)) a1++;
      if(adjacent_digits_only(i)) a2++;
    }
  }

  printf("answer1: %d\n", a1);
  printf("answer2: %d\n", a2);

  return 0;
}
