package com.example.tipcalculator.utill


fun calculatetotaltip(totalbill: Double, tippercentage: Int): Double {

    return if(totalbill > 1 &&
        totalbill.toString().isNotEmpty())
        (totalbill * tippercentage) / 100
    else 0.0
}

fun calculatetotalperperson(
    totalbill: Double,
    splitby: Int,
    tippercentage: Int

): Double{
    val bill = calculatetotaltip(totalbill = totalbill,
                                 tippercentage = tippercentage) + totalbill
    return (bill / splitby)

}