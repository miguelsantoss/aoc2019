#lang racket

(define (parse-file f)
  (let ([input (call-with-input-file* f (lambda (in) (read-line in)))])
    (map string->number (string-split input ","))))

(define (execute ins-l ip)
  (define (pos p)
    (list-ref ins-l p))

  (define (arg n)
    (pos (+ ip n)))

  (define (do-op op)
    (let ([res (op (pos (arg 1)) (pos (arg 2)))])
      (list-set ins-l (arg 3) res)))

  (let ([instruction (pos ip)])
    (case instruction
      [(1) (execute (do-op +) (+ ip 4))]
      [(2) (execute (do-op *) (+ ip 4))]
      [(99) ins-l])))

(define (initial-state ll noun verb)
  (list-set (list-set ll 1 noun) 2 verb))

(define (answer1 f)
  (let* ([instructions (parse-file f)]
         [p1 (initial-state instructions 12 2)])
    (car (execute p1 0))))

(define (answer2 f target)
  (define (exec-until ll)
      (for*/first ([n (in-range 99)]
                   [v (in-range 99)]
                   #:when (= target (car (execute (initial-state ll n v) 0))))
        (list n v)))
  (let* ([instructions (parse-file f)]
         [res (exec-until instructions)])
    (+ (* 100 (car res)) (car (cdr res)))))

(answer1 "input")
(answer2 "input" 19690720)
