; Space
draw (night) (
    lines ( (0, 0) (2000, 0) (2000, 2000) (0, 2000) )
)

; Yellow and orange stars
do (350 times) (
    with randoms ( _starX in range (0..1100), _starY in range (0..700) ) (
        draw (random (in set (yellow, orange))) (
            lines (
                (_starX, _starY)
                (_starX - 3, _starY + 6)
                (_starX - 9, _starY + 9)
                (_starX - 3, _starY + 12)
                (_starX, _starY + 18)
                (_starX + 3, _starY + 12)
                (_starX + 9, _starY + 9)
                (_starX + 3, _starY + 6)
                (_starX, _starY)
                rotate random (in range (0..180))
            )
        )
    )
)

; A black hole
rotate area (20) (
    draw (black) (
        circles (
            ((500, 60) 20 )
        )
    )

    ; Круги
    do (70 times) (
        draw (random (in set (red, orange, claret, violet, dark-red, bright-pink)), 1) (
            circles (
                (random (in union (circles (((500, 60) 6)))) 20 )
            )
        )
    )
    draw (yellow, 2) (
        circles (
            ((500, 60) 11)
        )
    )

    ; Овалы
    do (100 times) (
        with randoms (_w in range (30..60), _h in range (5..9), _color in range (0..7)) (
            draw (random (in set(red, orange, claret, violet, dark-red, bright-pink)), 1) (
                ovals ( ((500, 60 + _color), size _w x _h, sector from 140 size 250) )
            )
        )
    )
    draw (yellow, 3) (
        ovals (
            ((500, 56), size 30 x 10, sector from 240 size 55)
        )
    )
)

; The Sun
draw (orange) ( circles ( ((100, 350) 250) ) )

; Orbits
draw (orange, 1) (
    do (_orbitNum from 300 to 1200 step 150) (
        ovals ( ((100, 350) size _orbitNum x _orbitNum / 3) )
    )
)

; An asteroid belt
do (215 times) (
    draw (random (in set(gray, gray, silver, silver, claret, violet, bright-pink))) (
        with randoms (
            (_x, _y) in union (
                lines (
                    (580, 110)
                    (700, 150)
                    (850, 230)
                    (860, 300)
                    (730, 260)
                )
                lines (
                    (900, 330)
                    (960, 300)
                    (980, 400)
                    (830, 530)
                    (560, 630)
                    (500, 600)
                    (530, 570)
                    (600, 500)
                    (770, 440)
                )
            ),
            _scale in set (2, 2, 2, 2, 2, 3, 3, 4, 4, 4, 5, 5, 5),
            _fluctuation in range (0..8),
            _rotate in range (0..360)
        ) (
            lines (
                (_x, _y)
                (_x + 10 / _scale, _y - 18 / _scale)
                (_x + (19 + _fluctuation) / _scale, _y - 5 / _scale)
                (_x + 16 / _scale, _y + (7 + _fluctuation) / _scale)
                (_x + 30 / _scale, _y + 20 / _scale)
                (_x + 15 / _scale, _y + 30 / _scale)
                (_x - 5 / _scale, _y + (15 -_fluctuation) / _scale)
                (_x, _y + 10 / _scale)
                rotate _rotate
            )

            draw (black, 1) (
                lines (
                    (_x, _y)
                    (_x + 10 / _scale, _y - 18 / _scale)
                    (_x + (19 + _fluctuation) / _scale, _y - 5 / _scale)
                    (_x + 16 / _scale, _y + (7 + _fluctuation) / _scale)
                    (_x + 30 / _scale, _y + 20 / _scale)
                    (_x + 15 / _scale, _y + 30 / _scale)
                    (_x - 5 / _scale, _y + (15 - _fluctuation) / _scale)
                    (_x, _y + 10 / _scale)
                    rotate _rotate
                )
            )
        )
    )
)

; Mercury
draw (gray) ( circles ( ((170, 450 - 5) 25) ) )
; Venus
draw (dark-red) ( circles ( ((200 + 85, 470 + 20) 35) ) )
; Earth
draw (cyan) ( circles ( ((450, 510) 30) ) )
; Mars
draw (red) ( circles ( ((370, 120)  27) ) )
; Jupiter
draw (light-brown) ( circles ( ((550 + 150, 570)  90) ) )
; Saturn
draw (dark-brown) ( circles ( ((830, 100)  80) ) )
draw (dark-brown, 30) ( ovals ( ((830, 100) 140 x 30) ) )
draw (light-brown, 2) ( ovals ( ((830, 100), size 140 x 30, sector from 122 size 295) ) )

; Rocket
; Pathway
draw (red, 1) (
    curves (
        (from (460, 500) to (525, 430) towards (500, 400))
    )
)

rotate area (35) (
    ; Flame
    draw (orange) ( ovals ( ((525, 450) 9 x 30) ) )
    draw (blue) ( ovals ( ((525, 440) 5 x 20) ) )

    draw (red) (
        lines (
            (510, 360)
            (510, 430)
            (540, 430)
            (540, 360)
            (525, 320)
        )
        curves (
            (from (510, 360) to (525, 320) towards (510, 335))
            (from (540, 360) to (525, 320) towards (540, 335))
        )
        ovals (
            ( (525, 430), size 15 x 7, sector from 180 size 180 )
        )
    )

    ; Wings
    draw (claret) (
        lines (
            (510, 380)
            (500, 390)
            (495, 440)
            (500, 438)
            (510, 410)
        )
        lines (
            (540, 380)
            (550, 390)
            (555, 440)
            (550, 438)
            (540, 410)
        )
        lines (
            (523, 380)
            (527, 380)
            (527, 440)
            (523, 440)
        )
    )

    ; View port
    draw (yellow) (
        circles (
            ((525, 360) 7)
        )
    )

    ; Outline
    draw (claret, 2) (
        lines (
            (510, 360)
            (510, 430)
        )
        lines (
            (540, 360)
            (540, 430)
        )
        ovals (
            ( (525, 330), size 15 x 7, sector from 230 size 80 )
        )
        curves (
            (from (510, 360) to (525, 320) towards (510, 335))
            (from (540, 360) to (525, 320) towards (540, 335))
        )
        circles (
            ((525, 360) 7)
        )
    )
)


; Voyager
rotate area (330) (
    draw (violet) (
        ovals (
            ( (900, 250), size 30 x 70, sector from 270 size 180)
        )
    )
    draw (silver) (
        ovals (
            ( (900, 250) 10 x 70)
            ( (880, 250) 5 x 15)
        )
    )

    draw (white, 2) (
        lines (
            (909, 220)
            (880, 235)
            (909, 250)
        )

        lines (
            (909, 250)
            (880, 265)
            (909, 280)
        )

        lines (
            (909, 235)
            (885, 250)
            (909, 265)
        )
    )
    draw (silver, 2) (
        lines (
            (908, 220)
            (908, 280)
        )
    )
    draw (violet, 2) (
        lines (
            (910, 220)
            (910, 280)
        )
    )

    draw (claret) (
        lines (
            (925, 215)
            (930, 235)
            (930, 265)
            (925, 285)
            (950, 260)
            (950, 240)
        )

        circles (
            ( (950, 250) 15)
        )
    )

    draw (white, 2) (
        lines (
            (960, 195)
            (1000, 225)
            (960, 255)
        )
        lines (
            (960, 205)
            (1000, 235)
            (960, 265)
        )

        lines (
            (960, 235)
            (1000, 265)
            (960, 295)
        )
        lines (
            (960, 245)
            (1000, 275)
            (960, 305)
        )
    )
    do (_x from 950 to 970 step 1) (
        draw (blue, 1) (
            ovals (
                ( (_x, 250), size 5 x 60, sector from 90 size 75)
                ( (_x, 250), size 5 x 60, sector from 195 size 75)
            )
        )
    )

    draw (white, 2) (
        lines (
            (930, 210)
            (930, 170)
            (940, 170)
            (940, 210)
            (930, 190)
            (940, 170)
            (930, 170)
            (940, 190)
            (930, 210)
            (960, 210)
            (960, 200)
            (940, 200)
            (940, 210)
        )


        lines (
            (900, 300)
            (900, 390)
            (910, 390)
            (910, 300)
            (900, 300)
            (910, 330)
            (900, 360)
            (910, 390)
            (900, 390)
            (910, 360)
            (900, 330)
            (910, 300)
            rotate 40
        )

        lines (
            (931, 306)
            (960, 285)
            (938, 314)
        )
    )

    do (_x from 950 to 970 step 1) (
        draw (cornflower, 1) (
            ovals (
                ( (_x, 250), size 5 x 60, sector from 270 size 180)
            )
        )
    )
)


; Comet
; Orbit
draw (light-blue, 1) ( curves ( (from (0, 560) to (600, 0) towards (500, 400)) ) )

rotate area (310) (
    ; Tail
    do (100 times) (
        with randoms (
            (_fromX, _fromY) in union ( circles ( ((470, 230) 7) ) ),
            (_toX, _toY) in union ( circles ( ((550, 230) 25) ) ),
            (_towardsX, _towardsY) in union ( circles ( ((510, 230) 10) ) )
        ) (
            draw (random (in set(cornflower, cream, blue, white)), 1) (
                curves ( (from (_fromX, _fromY) to (_toX, _toY) towards (_towardsX, _towardsY)) )
            )
        )
    )

    ; Body
    draw (light-blue) ( ovals ( ((455, 230) 30 x 10) ) )

    draw (blue, 1) (
        ovals ( ((455, 230) 30 x 10) )

        do (20 times) (
            with randoms (
                    (_fromX, _fromY) in union (
                        ovals (
                            ((455, 230), size 30 x 10)
                        )
                    ),
                    (_toX, _toY) in union (
                        ovals (
                            ((455, 230), size 30 x 10)
                        )
                    )
                ) (
                    lines ( (_fromX, _fromY) (_toX, _toY) )
                )
            )
        )
)

; Tardis
rotate area (8) (
    with randoms (_x in set(970), _y in set (545)) (
        ; Silhouette
        draw (tardis) (
            lines (
                (_x - 30, _y + 10)
                (_x - 30, _y + 13)
                (_x - 35, _y + 15)
                (_x - 30, _y + 115)
                (_x, _y + 140)
                (_x + 30, _y + 115)
                (_x + 35, _y + 15)
                (_x + 30, _y + 13)
                (_x + 30, _y + 10)
                (_x, _y)
            )
        )

        ; Outline
        draw (cream, 1) (
            lines (
                (_x - 30, _y + 10)
                (_x - 30, _y + 13)
                (_x - 35, _y + 15)
                (_x - 30, _y + 115)
                (_x, _y + 140)
                (_x + 30, _y + 115)
                (_x + 35, _y + 15)
                (_x + 30, _y + 13)
                (_x + 30, _y + 10)
                (_x, _y)
            )

            lines (
                (_x - 30, _y + 10)
                (_x, _y + 20)
                (_x + 30, _y + 10)
                (_x, _y + 5)
            )

            lines (
                (_x, _y + 5)
                (_x, _y + 140)
            )
        )

        draw (yellow) (
            ovals (
                ((_x, _y + 4) 3 x 4)
            )
        )

        draw (cream, 3) (
            lines (
                (_x - 30, _y + 15)
                (_x, _y + 25)
            )
            lines (
                (_x, _y + 25)
                (_x + 30, _y + 15)
            )
            lines (
                (_x - 32, _y + 20)
                (_x - 3, _y + 30)
                (_x - 3, _y + 34)
                (_x - 32, _y + 24)
            )
            lines (
                (_x + 3, _y + 30)
                (_x + 32, _y + 20)
                (_x + 32, _y + 24)
                (_x + 3, _y + 34)
            )
        )

        ; Doors
        draw (cream, 3) (
            ; Left
            lines (
                (_x - 30, _y + 26)
                (_x - 26, _y + 112)
            )
            lines (
                (_x - 18, _y + 30)
                (_x - 16, _y + 120)
            )
            lines (
                (_x - 5, _y + 34)
                (_x - 4, _y + 130)
            )

            lines (
                (_x - 26, _y + 112)
                (_x - 4, _y + 130)
            )
            lines (
                (_x - 26, _y + 95)
                (_x - 4, _y + 112)
            )
            lines (
                (_x - 26, _y + 75)
                (_x - 5, _y + 89)
            )
            lines (
                (_x - 26, _y + 55)
                (_x - 5, _y + 66)
            )


            ; Right
            lines (
                (_x + 5, _y + 34)
                (_x + 6, _y + 130)
            )
            lines (
                (_x + 18, _y + 30)
                (_x + 16, _y + 120)
            )
            lines (
                (_x + 30, _y + 26)
                (_x + 26, _y + 112)
            )

            lines (
                (_x + 6, _y + 130)
                (_x + 26, _y + 112)
            )
            lines (
                (_x + 6, _y + 112)
                (_x + 26, _y + 95)
            )
            lines (
                (_x + 7, _y + 89)
                (_x + 26, _y + 75)
            )
            lines (
                (_x + 7, _y + 66)
                (_x + 26, _y + 55)
            )
        )

        draw (cream) (
            lines (
                (_x + 8, _y + 85)
                (_x + 8, _y + 68)
                (_x + 15, _y + 64)
                (_x + 14, _y + 80)
            )
        )
    )
)