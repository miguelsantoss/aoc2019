#lang racket/base

(define (fuel-for-1 mass)
  (- (floor (/ mass 3)) 2))

(define (fuel-for-2 mass)
  (define (fuel-2 r tf)
    (let ([fuel (fuel-for-1 r)])
      (if (<= fuel 0)
          tf
          (fuel-2 fuel (+ fuel tf)))))
  (fuel-2 mass 0))

(define (answer f calc)
  (define (parse-fuel in fuel)
    (let ([res (read-line in)])
      (if (eof-object? res)
          fuel
          (let* ([fuel-module (calc (string->number res))]
                 [new-list (append fuel (list fuel-module))])
            (parse-fuel in new-list)))))
  (call-with-input-file* f
    (lambda (in) (apply + (parse-fuel in '())))))

(answer "input" fuel-for-1)
(answer "input" fuel-for-2)
