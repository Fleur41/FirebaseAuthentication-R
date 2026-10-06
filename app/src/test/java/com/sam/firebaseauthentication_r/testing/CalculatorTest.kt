package com.sam.firebaseauthentication_r.testing

import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    lateinit var calculator: Calculator

    @Before
    fun setup(){
        println("This will be printed before each test case")
        calculator = Calculator()
    }

    @After
    fun tearDown(){
        println("This will be printed after each test case")
        calculator = Calculator()
    }
    @Test
    fun testAddition(){
        // Arrange
//        val calculator = Calculator()
        // Action
        val result = calculator.add(4, 4)
        // Assert
        Assert.assertEquals(8, result)

    }

    @Test
    fun testSubtraction(){
        val calculator = Calculator()
        val result = calculator.subtract(8, 4)
        Assert.assertEquals(4, result)
    }

    @Test
    fun testMultiplication(){
        val result = calculator.multiply(4, 4)
        Assert.assertEquals(16, result)
    }

    @Test(expected = IllegalArgumentException::class)
//    fun testDivisionWithException(){
    fun `dividing by zero should throw an exception`() {
        val result = calculator.divide(8, 0)
        Assert.assertEquals(2, result)
    }
}