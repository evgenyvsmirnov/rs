; Sky
draw (cyan) (
    lines ( (0, 0) (2000, 0) (2000, 2000) (0, 2000) )
)

; Sun
draw (orange) (
    do (_ray from 0 to 360 step 10) (
        ovals (
            ((200, 200) 190 x 15 rotate _ray)
        )
    )
)

; Land
draw (dark-brown) (
    ovals (
        ((900, 800) 800 x 400)
    )
)

do (40000 times) (
    with randoms (
        (_x, _y) in union (
            ovals (
                ((900, 800) 800 x 400)
            )
        ),
        _height in range (20..40),
        _fluctuation in range (5..7),
        _curve in range (5..10)
    ) (
        draw (random (in set(green, olive, olive, lime))) (
            curves (
                (from(_x, _y) to (_x - _fluctuation, _y - _height) towards (_x - _curve, _y - _height / 2))
            )
        )
    )
)

; Flower

; Leaves
draw (green) (
    curves (
        (from(780, 460) to (670, 400) towards (700, 480))
        (from(780, 460) to (670, 400) towards (690, 300))
    )
    curves (
        (from(760, 400) to (760, 370) towards (810, 320))
    )
)
draw (olive, 2) (
    curves (
        (from(780, 460) to (670, 400) towards (700, 480))
        (from(780, 460) to (670, 400) towards (690, 300))
        (from(780, 455) to (690, 362) towards (700, 420))
        (from(730, 425) to (720, 375) towards (700, 400))
        (from(750, 440) to (695, 440) towards (730, 450))
        (from(734, 430) to (671, 400) towards (690, 430))

        (from(760, 400) to (760, 370) towards (810, 320))
        (from(760, 385) to (785, 351) towards (770, 370))
    )
)

do (_ray from 0 to 20 step 1) (
    draw (green, 1) (
        curves (
            (from(800 + _ray, 500) to (800 + _ray, 250) towards (700, 400))
        )
    )
)
draw (olive, 2) (
    curves (
        (from(800, 500) to (800, 250) towards (700, 400))
        (from(820, 500) to (820, 250) towards (700, 400))
    )
)

do (_ray from 0 to 360 step 20) (
    draw (random (in set(cream, white, bright-pink, cornflower, pink-violet))) (
        ovals (
            ((800, 250) 100 x 20 sector from 90 size 180 rotate _ray)
        )
    )
)

draw (gold) (
    circles (
        ((800, 250) 50)
    )
)
do (200 times) (
    with randoms (
        (_x, _y) in union (
            circles (
                ((800, 250) 47)
            )
        )
    ) (
        draw (random (in set(yellow, orange))) (
            circles (
                ( (_x, _y) 4 )
            )
        )
    )
)