#lang racket

(define (read-file in ll)
  (let ([input (read-line in)])
    (if (eof-object? input) ll (read-file in (append ll (list input))))))

(define (parse-file f)
  (let ([input (call-with-input-file* f (lambda (in) (read-file in '())))])
    (map (lambda (el) (string-split el ",")) input)))

(parse-file "input")
