<?php

require_once 'SimpleCalc.php';
use Gajdaw\TddExamples\SimpleCalc\SimpleCalc;

class SimpleCalcTest extends PHPUnit_Framework_TestCase
{

    public function testAdd()
    {
        $this->assertEquals(3, SimpleCalc::add(1, 2));
        $this->assertEquals(20.0, SimpleCalc::add(10.5, 9.5));
        $this->assertEquals(500.0, SimpleCalc::add(499.1, 0.9));
    }

    public function testSubtract()
    {
        $this->assertEquals(1, SimpleCalc::subtract(7, 6));
        $this->assertEquals(-10, SimpleCalc::subtract(10, 20));
    }

    /**
     * @dataProvider getMultiplyData
     *
     */
    public function testMultiply($a, $b, $result)
    {
        $this->assertEquals($result, SimpleCalc::multiply($a, $b));
    }

    public function getMultiplyData()
    {
        return array(
            array(1, 2, 2),
            array(2, 5, 10),
            array(2, 10, 20),
            array(100, 0.01, 1),
        );
    }

    /**
     * @dataProvider getDivideData
     */
    public function testDivide($a, $b, $result)
    {
        $this->assertEquals($result, SimpleCalc::divide($a, $b));
    }

    /**
     * @expectedException InvalidArgumentException
     */
    public function testFByOneExceptionMG()
    {
        SimpleCalc::fNewfunctionMg(1, 0);
    }

    public function getMultiplyDataMG() {
        return array(
            array(2, -3)
        );
    }

    /**
     * @dataProvider getMultiplyDataMG
     */
    public function testFByOneMG($x, $result) {
        $this->assertEquals($result, SimpleCalc::fNewfunctionMg($x));
    }

    public function getDivideData()
    {
        return array(
            array(10, 2, 5),
            array(20, 5, 4),
            array(7, 2, 3.5),
        );
    }

    /**
     * @expectedException InvalidArgumentException
     */
    public function testDivideByZero()
    {
        SimpleCalc::divide(30, 0);
    }

    public function testZero()
    {
        $this->assertEquals(0, SimpleCalc::zero());
    }

    public function testOne()
    {
        $this->assertEquals(1, SimpleCalc::one());
    }

    /**
     * @dataProvider getIfMultiplyInversesDivideData
     *
     */
    public function testIfMultiplyInversesDivide($a, $b)
    {
        $this->assertEquals($a, SimpleCalc::multiply(SimpleCalc::divide($a, $b), $b));
        $this->assertEquals($a, SimpleCalc::divide(SimpleCalc::multiply($a, $b), $b), '', 0.001);
    }

    public function getIfMultiplyInversesDivideData()
    {
        return array(
            array(100, 2),
            array(97, 37),
            array(123456345789, 0.000000000037),
        );
    }

    /**
     * @dataProvider getOneDivXPlusOneData
     *
     */
    public function testOneDivXPlusOne($x, $result)
    {
        $this->assertEquals($result, SimpleCalc::oneDivXPlusOne($x), ', 0.00000001');
    }

    public function getOneDivXPlusOneData()
    {
        return array(
            array(1, 0.5),
            array(0, 1),
            array(10, 1/11),
        );
    }

    public function testSixtyNine()
    {
        $this->assertEquals(69, SimpleCalc::sixtynine());
    }

    public function testEleven()
    {
        $this->assertEquals(11, SimpleCalc::Eleven());

    }

    /**
     * @dataProvider getOneDivXData
     *
     */
    public function testOneDivX($x, $result)
    {
        $this->assertEquals($result, SimpleCalc::oneDivX($x));
    }
    /**
     * @expectedException InvalidArgumentException
     */
    public function testOneDIvZero()
    {
        SimpleCalc::oneDivX(0);
    }
    /**
     * @dataProvider getOneDivXData
     *
     */
    public function getOneDivXData()
    {
        return array(
            array(1, 1),
            array(2, 0.5),
            array(10, 0.1),
        );
    }

    public function testExponentiation()
    {
        $this->assertEquals(36.0, SimpleCalc::exponentiation(6,2));
        $this->assertEquals(15.625, SimpleCalc::exponentiation(2.5,3));
        $this->assertEquals(4.0, SimpleCalc::exponentiation(2,2));
        $this->assertEquals(1.0, SimpleCalc::exponentiation(20,0));
        $this->assertEquals(5.0, SimpleCalc::exponentiation(5,1));
    }

    public function testLinearEquation()
    {
        $this->assertEquals(0.5, SimpleCalc::LinearAquationAxb(2,1));
    }

    public function testOneDivSquareXMinus1()
    {
        $this->assertEquals(1 / 3, SimpleCalc::oneDivSquareXMinus1(2));
    }

    public function testXSquarePlus1DivideByXMinus1()
    {
        $this->assertEquals(5, SimpleCalc::xSquarePlus1DivideByXMinus1(2));
        $this->assertEquals(5, SimpleCalc::xSquarePlus1DivideByXMinus1(3));
    }

    /**
     * @expectedException InvalidArgumentException
     */
    public function testXSquarePlus1DivideByXMinus1DivideByZero()
    {
        SimpleCalc::xSquarePlus1DivideByXMinus1(1);
    }

    /**
     * @dataProvider getFOnePlusXOneMinusXData
     *
     */
    public function testFOnePlusXOneMinusX($x,$result)
    {
        $this->assertEquals($result, SimpleCalc::fOnePlusXOneMinusX($x));
    }

    public function getFOnePlusXOneMinusXData()
    {
        return array(
            array(2, -3),
        );
    }

    /**
     * @expectedException InvalidArgumentException
     */
    public function testFOnePlusXOneMinusXException()
    {
        SimpleCalc::fOnePlusXOneMinusX(1);
    }

    /**
     * @dataProvider getTwentyEightPlusXDivOneMinusXData
     *
     */
    public function testTwentyEightPlusXDivOneMinusX($x, $result)
    {
        $this->assertEquals($result, SimpleCalc::twentyEightPlusXDivOneMinusX($x));
    }

    public function getTwentyEightPlusXDivOneMinusXData()
    {
        return array(
            array(0, 28)
        );
    }


    /**
     * @expectedException InvalidArgumentException
     */
    public function testTwentyEightPlusXDivOneMinusXException()
    {
        SimpleCalc::twentyEightPlusXDivOneMinusX(1);
    }


     /**
     * @dataProvider getNinePlusXMultiplyFive
     *
     */
    public function testNinePlusXMultiplyFive($x, $result)
    {
        $this->assertEquals($result, SimpleCalc::NinePlusXMultiplyFive($x));
    }

    public function getNinePlusXMultiplyFive()
    {
        return array(
            array(1, 50)
        );
    }

    /**
     * @dataProvider getOneDivXPlusFive
     *
     */

    public function testOneDivXPlusFive($x, $result){
        $this->assertEquals($result, SimpleCalc::oneDivXPlusFive($x));
    }

    public function getOneDivXPlusFive(){
        return array(
            array(5, 0.1),
            array(-5, 0)
        );
    }

    /**
     * @dataProvider getTwoMultiplyOneMinusTwoDivXPlusTen
     *
     */

    public function testTwoMultiplyOneMinusTwoDivXPlusTen($x, $result){
        $this->assertEquals($result, SimpleCalc::twoMultiplyOneMinusTwoDivXPlusTen($x));
    }

    public function getTwoMultiplyOneMinusTwoDivXPlusTen(){
        return array(
            array(4, 2 / 7),
            array(-4, -2)
        );
    }
}
