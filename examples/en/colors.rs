; Ovals of all available colors
draw (cream) (
    lines ((0, 0) (0, 2000) (1500, 2000) (1500, 0))
)
do (2000 times) (
    with randoms (
        _x in range (0..2000),
        _y in range (0..1500),
        _r in range (0..180)) (
        draw (random (in set (
                        red, green, black, blue, pink, white, cyan, gray, orange, yellow, magenta,
                        olive, lime, night, claret, gold, violet, bright-pink, pink-violet,
                        dark-brown, light-brown, light-blue, dark-red, cream, cornflower, tardis-blue))) (
            ovals (
                ((_x, _y) 40 x 15 rotate _r)
            )
        )
    )
)
