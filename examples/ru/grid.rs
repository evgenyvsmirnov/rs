; Сетка 1000 x 1000 (шаг 33 и 100)
рисовать (серый, 3) (
    выполнить (_х от 0 до 1000 шаг 100) (
        линии (
            (_х, 0)
            (_х, 800)
        )
    )

    выполнить (_у от 0 до 1000 шаг 100) (
        линии (
            (0, _у)
            (1000, _у)
        )
    )
)

рисовать (серый, 1) (
    выполнить (_х от 0 до 1000 шаг 33) (
        линии (
            (_х, 0)
            (_х, 800)
        )
    )

    выполнить (_у от 0 до 1000 шаг 33) (
        линии (
            (0, _у)
            (1000, _у)
        )
    )
)