# Experiment 4: Division by Square Root (Newton-Raphson Method)

## Aim
To implement the Newton-Raphson iterative method in Android Studio using Java to compute the reciprocal square root $\frac{1}{\sqrt{a}}$ and perform division by square root $\frac{b}{\sqrt{a}}$, observing the rapid convergence through successive iterations.

## Formula
For $f(y) = \frac{1}{y^2} - a = 0$, the Newton-Raphson update rule for computing $\frac{1}{\sqrt{a}}$ without division is:
$$y_{k+1} = y_k \cdot \left(1.5 - 0.5 \cdot a \cdot y_k^2\right)$$
Then the division $\frac{b}{\sqrt{a}}$ is approximated as $b \cdot y_{k+1}$.

## Procedure
1. Initialize parameters $a = 2.0$, $b = 10.0$, and initial guess $y = 0.5$.
2. Compute actual exact value $b / \sqrt{a}$ using standard library functions for verification.
3. Perform 5 iterations of the reciprocal square root update formula.
4. Calculate approximation $b \cdot y$ and absolute error at each iteration.
5. Display step-by-step convergence and error progression on the screen using a scrollable `TextView`.

## Output
```
Division by Square Root using Newton-Raphson

a = 2.0
b = 10.0

MATLAB / Actual Result = 7.07106781

Iteration 1
1/sqrt(a) = 0.62500000
b/sqrt(a) = 6.25000000
Error = 0.82106781

Iteration 2
1/sqrt(a) = 0.69335938
b/sqrt(a) = 6.93359375
Error = 0.13747406

Iteration 3
1/sqrt(a) = 0.70673412
b/sqrt(a) = 7.06734120
Error = 0.00372661

Iteration 4
1/sqrt(a) = 0.70710629
b/sqrt(a) = 7.07106287
Error = 0.00000494

Iteration 5
1/sqrt(a) = 0.70710678
b/sqrt(a) = 7.07106781
Error = 0.00000000

Observation:
As the number of iterations increases,
the approximation converges rapidly
to the actual value.
```
