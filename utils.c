#include "utils.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

void read_file(char* filename, char** content) {
  FILE *fp = fopen(filename, "r");

  if (fp == NULL) {
    printf("Could not open file %s\n", filename);
    exit(EXIT_FAILURE);
  }

  fseek(fp, 0, SEEK_END);
  long size = ftell(fp);
  fseek(fp, 0, SEEK_SET);

  char *str = malloc(size + 1);
  fread(str, 1, size, fp);
  str[size] = 0;

  *content = str;
  fclose(fp);
}

char* trim(char* str) {
  while(isspace((unsigned char) *str)) str++;

  if (*str == 0) {
    return str;
  }

  char *end = str + strlen(str) - 1;
  while(end > str && isspace((unsigned int) *end)) end--;

  end[1] = 0;

  return str;
}

int count_delimiters(char* str, char* delim) {
  int count = 0;
  char *curr = str;

  while((curr = strpbrk(curr, delim))) {
    count++;
    curr++;
  }

  return count;
}

int max(int a, int b) {
  if (a > b) return a;
  return b;
}

