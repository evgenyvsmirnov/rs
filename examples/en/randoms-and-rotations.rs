; Random coordinates from a union of shapes with rotation
draw (blue, 3) (
    ovals(
        ((800, 300) 300 x 200 rotate random(in set (45, 90)))
    )

   with randoms (_factor in range (2..10)) (
        lines (
            (50, 50)
            (50, 50 + 350 / _factor + 1 - 4 / 2 * (2 + 1))
            (50 + 50 / _factor, 50 + 350 / _factor)
            (50 + 220 / _factor, 50 + 220 / _factor)
            (50 + 80 / _factor, 50 - 25 / _factor)
            rotate random(in range (0..360))
        )

        lines (
            (10, 10)
            (10, 50)
            rotate random(in range (0..360))
        )
   )

   do (_factor from 10 to 100 step 10) (
        ovals(
            ( (80, 450)
                size 70 x 20,
                sector from random (in range (0..360)) size random (in range (0..360)),
                rotate random (in range (0..180)))
        )
   )
)

rotate area (random (in range(0..360))) (
    draw (red, 3) (
        lines (
            (800, 200)
            (800, 500)
            (900, 500)
            (900, 200)
        )

        draw (red) (
            ovals (
                ((890, 350) 100 x 20 rotate 10)
            )
        )

        ovals (
            ((890, 350) 100 x 20 rotate 80)
        )

        curves (
            (from (800, 200) to (900, 200) towards (900, 500) rotate 40)
        )
    )
)

rotate area (random (in range (1..90)))
(
    do (random (in set (100, 200)) times)
    (
        circles(
            ((400, 300) 200)
        )

        ovals(
            ((800, 300) size 200 x 50 sector from 300 size 180)
        )

        lines (
            (50, 50)
            (50, 400)
            (100, 400)
            (270, 270)
            (130, 35)
        )

        curves (
            (from (100, 500) to (300, 600) towards (900, 700))
        )


        with randoms (
            _numberOfLeaves in range (30..90),
            (_treeX, _treeY) in union (
                circles (
                    ((400, 300) 200)
                )
                ovals(
                    ((800, 300) size 200 x 50 sector from 300 size 180)
                )
                lines (
                    (50, 50)
                    (50, 400)
                    (100, 400)
                    (270, 270)
                    (130, 35)
                )
                curves (
                    (from (100, 500) to (300, 600) towards (900, 700))
                )
            )
        )
        (
            draw (black, 1)
            (
                circles(
                    ((_treeX, _treeY) 40)
                )
            )

            do (_numberOfLeaves times)
            (
                draw (random (in set(yellow, green, orange, red)), 1)
                (
                    circles
                    (
                        (
                            random(
                                in union(
                                    circles(
                                        ((_treeX, _treeY) 40)
                                    )
                                )
                            ) 2
                        )
                    )
                )
            )
        )
    )
)