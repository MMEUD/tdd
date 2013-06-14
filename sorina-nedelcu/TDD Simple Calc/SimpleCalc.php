<?php

/**
 * SimpleCalc library
 *
 * This file is part of the TDD PHP project:
 *
 *     https://github.com/tdd-php
 *
 * (c)2013 Włodzimierz Gajda <gajdaw@gajdaw.pl>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */

namespace Gajdaw\TddExamples\SimpleCalc;

/**
 * SimpleCalc performs math operations:
 * - addition
 * - subtraction
 * - multiplication
 * - division
 * - zero
 *
 * @package PHPTDD
 * @author  Włodzimierz Gajda <gajdaw@gajdaw.pl>
 *
 */
class SimpleCalc
{

    /**
     * Addition
     *
     * @param mixed $a First number (integer of float)
     * @param mixed $b Second number (integer of float)
     *
     * @return mixed The sum of two numbers (integer of float)
     */
    public static function add($a, $b)
    {
        return $a + $b;
    }

    /**
     * Subtraction
     *
     * @param mixed $a First number (integer of float)
     * @param mixed $b Second number (integer of float)
     *
     * @return mixed The difference of two numbers (integer of float)
     */
    public static function subtract($a, $b)
    {
        return $a - $b;
    }

    /**
     * Multiplication
     *
     * @param mixed $a First number (integer of float)
     * @param mixed $b Second number (integer of float)
     *
     * @return mixed The product of two numbers (integer of float)
     */
    public static function multiply($a, $b)
    {
        return $a * $b;
    }

    /**
     * Division
     *
     * @param mixed $a First number (integer of float)
     * @param mixed $b Second number (integer of float)
     *
     * @return float The result of division of two numbers
     * @throws \InvalidArgumentException The exception is thrown when the divisor $b is equal to 0
     */
    public static function divide($a, $b)
    {
        if ($b == 0) {
            throw new \InvalidArgumentException('Divisor must not be equal to 0.');
        }

        return $a / $b;
    }

    /**
     * Zero
     *
     * @return int The result is always equal to 0
     */
    public static function zero()
    {
        return 0;
    }

    /**
     * One
     *
     * @return int The result is always equal to 1
     */
    public static function one()
    {
        return 1;
    }

    /**
     * Value of f(x) = 1 / (x + 1)
     *
     * @param mixed $x Integer of float
     *
     * @return float The result f(x)
     */
    public static function oneDivXPlusOne($x)
    {
        return 1 / ($x + 1);
    }

    /**
     * Sixty nine
     *
     * @return int The result is always equal to 69
     */
    public static function sixtyNine()
    {
        return 69;
    }

    /**
     * Eleven
     *
     * @return float The result is always equal to 11
     */
    public static function eleven()
    {
        return 11;
    }

    /**
     * oneDivX
     *
     * @param mixed $x First number (integer of float)
     *
     * @return float The result of division of 1/x
     * @throws \InvalidArgumentException The exception is thrown when the divisor $b is equal to 0
     */
    public static function oneDivX ($x)
    {
        if ($x == 0) {
            throw new \InvalidArgumentException('Divisor must not be equal to 0.');
        }

        return 1/$x;
    }

    /**
     * Exponentiation
     *
     * @param mixed $a Base (integer of float)
     * @param mixed $e Exponent (integer)
     *
     * @return float The result of $a^$e
     */
    public static function exponentiation($a, $e)
    {
        if ($e == 0) {
            return 1;
        } elseif ($e == 1) {
            return $a;
        }

        $result = $a;
        for ($i = 2; $i <= $e; $i++) {
            $result = $result * $a;
        }

        return $result;
    }

    /**
     * Solution of ax=b
     *
     * @param mixed $a First number (integer or float)
     * @param mixed $b Second number (integer or float)
     *
     * @return float The result of x = b / a
     */

    public static function LinearAquationAxb($a, $b)
    {
        return $b / $a;
    }

    /**
     * Result of 1 / (x^2 - 1)
     *
     * @param mixed $x Argument (integer or float)
     *
     * @return float The result of 1 / (x^2 - 1)
     */
    public static function oneDivSquareXMinus1($x)
    {
        return 1 / ($x * $x - 1);
    }

    /**
     * Function f(x) = (x * x + 1) / (x - 1)
     *
     * @param mixed $x Argument (integer or float)
     *
     * @return float The result of (x * x + 1) / (x - 1)
     * @throws \InvalidArgumentException The exception is thrown when the divisor $x is equal to 1
     */
    public static function xSquarePlus1DivideByXMinus1($x)
    {
        if($x == 1) {
            throw new \InvalidArgumentException('Divisor must not be equal to 0.');
        }

        return ($x * $x + 1) / ($x - 1);
    }

     /**
     * Function f(x) = (1 + x) / (1 - x)
     *
     * @param mixed $x Argument (integer of float)
     *
     * @return float The result of f(x) = (1 + x) / (1 - x)
     * @throws \InvalidArgumentException The exception is thrown when x = 1
     */
    public static function fOnePlusXOneMinusX($x)
    {
        if ($x == 1) {
            throw new \InvalidArgumentException('$x must not be equal to 1.');
        }

        return (1 + $x)  / (1 - $x);
    }


    /*
     * Function f(x) = (1 + x) / (1 - x)
     * @param mixed $x First number (integer of float)
     * 
     * @return float The result of f(x) = (1 + x) / (1 - x)
     * @throws \InvalidArgumentException The exception is thrown when x =1
     */
    public static function fNewfunctionMg($x)
    {
        if ($x == 1) {
            throw new \InvalidArgumentException('$x must by equal to 1.');
        }

        return (1 + $x)/(1 - $x);
    }
    /**
     * Function f(x) = (28+x)/(1-x)
     *
     * @param mixed $x Argument (integer of float)
     *
     * @return float The result of (228+x)/(1-x)
     * @throws \InvalidArgumentException The exception is thrown when the divisor $b is equal to 1
     *
     */

    public static function twentyEightPlusXDivOneMinusX($x)
    {
        if ($x == 1) {
            throw new \InvalidArgumentException('Divisor must not be equal to 0.');
        }

        return ((28 + $x) / ( 1 - $x ));
    }

    /**
     * Function f(x) = (9+x)*5
     *
     * @param mixed $x Argument (integer of float)
     *
     * @return float The result of (9+x)*5
     *
     */
    public static function NinePlusXMultiplyFive($x)
    {
        return (9 + $x) * 5;
    }
    
    /*
     * F(x) = 1/(x+5)
     * 
     * @param mixed $x Argument (integer or float)
     * 
     * @return float The result of F(x)
     */
    
    public static function oneDivXPlusFive($x){
        if ($x == -5) {
            throw new \InvalidArgumentException('Divisor must not be equal to -5.');
        }
        
        return 1/($x+5);
    }


    /*
    * F(x) = 2*(x-2)/(x+10)
    *
    * @param mixed $x Argument (integer or float)
    *
    * @return float The result of F(x)
    */

    public static function twoMultiplyOneMinusTwoDivXPlusTen($x){
        if ($x == -10) {
            throw new \InvalidArgumentException('Divisor must not be equal to -10.');
        }

        return 2*($x-2)/($x+10);
    }
}