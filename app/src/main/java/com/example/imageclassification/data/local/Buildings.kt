package com.example.imageclassification.data.local

import com.example.imageclassification.R

val buildings: List<Building> = mutableListOf(
    Building(
        0,
        "القلعة",
        "Castle",
        R.drawable.castle_thumb,
        null,
        mutableListOf(
            Floor(0, "الدور الارضي", mutableListOf<Hall>(), 0),
            Floor(1, "الدور الاول", mutableListOf<Hall>(), 0),
            Floor(2, "الدور الثاني", mutableListOf<Hall>(), 0)
        ),
    ),
    Building(
        1,
        "المبنى التعليمي",
        "Main",
        R.drawable.main_thumb,
        null,
        mutableListOf<Floor>(
            Floor(
                0,"الدور الارضي", mutableListOf<Hall>(
                    Hall(0, "معمل ١", mutableListOf<Int>(
                        R.drawable.laboratory_1_1, R.drawable.laboratory_1_1,
                    ), 1, 0)
                ), 1
            )
            ,Floor(
                1,"الدور الاول", mutableListOf<Hall>(
                    Hall(0, "المكتبة", mutableListOf<Int>(
                        R.drawable.library_1, R.drawable.library_2,R.drawable.library_3
                        , R.drawable.library_4,R.drawable.library_5, R.drawable.library_6,
                    ), 1, 1)
                    ,Hall(1, "معمل تربيه خاصه ", mutableListOf<Int>(
                        R.drawable.sp_edu_1, R.drawable.sp_edu_2,R.drawable.sp_edu_3
                        , R.drawable.sp_edu_4,R.drawable.sp_edu_5
                    ), 1, 1)
                ), 1)
            ,Floor(
                2,"الدور الثاني", mutableListOf<Hall>(
                    Hall(0, "مدرج مجدي العدوي", mutableListOf<Int>(), 1, 2)
                ), 1)
            ,Floor(
                3,"الدور الثالث", mutableListOf<Hall>(
                    Hall(0, "مدرج سلوي الملا", mutableListOf<Int>(
                        R.drawable.hall_salwa_1, R.drawable.hall_salwa_2,R.drawable.hall_salwa_3, R.drawable.hall_salwa_4,
                    ), 1, 3)
                ), 1)
            ,Floor(
                4,"الدور الرابع", mutableListOf<Hall>(
                    Hall(0, "مدرج فاروق التلاوي", mutableListOf<Int>(
                        R.drawable.hall_farouk_1, R.drawable.hall_farouk_2,R.drawable.hall_farouk_3
                        , R.drawable.hall_farouk_4,R.drawable.hall_farouk_5,R.drawable.hall_farouk_6
                        ,R.drawable.hall_farouk_7
                    ), 1, 4)
                ), 1)
        )
    ),
    Building(
        2,
        "مبني اقتصاد",
        "Section",
        R.drawable.section_thumb,
        null,
        mutableListOf<Floor>(
            Floor(0, "الدور الارضي", mutableListOf<Hall>(
                Hall(0, "اتيليه الخزف", mutableListOf<Int>(
                    R.drawable.khazaf_1, R.drawable.khazaf_2,R.drawable.khazaf_3,R.drawable.khazaf_4,
                    R.drawable.khazaf_5,R.drawable.khazaf_6,R.drawable.khazaf_7,
                ), 2, 0),
                Hall(1, "اتيليه النحت", mutableListOf<Int>(
                    R.drawable.naht_1, R.drawable.naht_2,R.drawable.naht_3,R.drawable.naht_4,
                    R.drawable.naht_5,
                ), 2, 0),
                Hall(2, "مدرج امال صادق", mutableListOf<Int>(
                    R.drawable.hall_amal_1, R.drawable.hall_amal_2,R.drawable.hall_amal_3,R.drawable.hall_amal_4,
                    R.drawable.hall_amal_5,R.drawable.hall_amal_6,
                ), 2, 0),
                Hall(3, "مدرج سيد صبحي", mutableListOf<Int>(
                    R.drawable.hall_sayed_1, R.drawable.hall_sayed_2,R.drawable.hall_sayed_3,
                ), 2, 0)
            ), 2),
            Floor(1, "الدور الاول", mutableListOf<Hall>(
                Hall(0, "معمل التغذيه 1", mutableListOf<Int>(
                    R.drawable.lab_feeding_1, R.drawable.lab_feeding_2,R.drawable.lab_feeding_3,R.drawable.lab_feeding_4,
                    R.drawable.lab_feeding_5,R.drawable.lab_feeding_6,
                ), 2, 1),
                Hall(1, "معمل الحاسب الالي", mutableListOf<Int>(
                    R.drawable.lab_cs_1, R.drawable.lab_cs_2,R.drawable.lab_cs_3,R.drawable.lab_cs_4,
                    R.drawable.lab_cs_5,
                ), 2, 1),
                Hall(2, "معمل الكيمياء", mutableListOf<Int>(
                    R.drawable.lab_chem_1, R.drawable.lab_chem_2,R.drawable.lab_chem_3,R.drawable.lab_chem_4,
                    R.drawable.lab_chem_5,R.drawable.lab_chem_6,R.drawable.lab_chem_7,R.drawable.lab_chem_8,
                    R.drawable.lab_chem_9,
                ), 2, 1),
                Hall(3, "معمل علم نفس", mutableListOf<Int>(
                    R.drawable.lab_psycho_1, R.drawable.lab_psycho_2,R.drawable.lab_psycho_3,
                    R.drawable.lab_psycho_4,R.drawable.lab_psycho_5,
                ), 2, 1)
            ), 2),
            Floor(2, "الدور التاني", mutableListOf<Hall>(
                Hall(0, "اتيليه 1", mutableListOf<Int>(
                    R.drawable.at_1_1, R.drawable.at_1_2,R.drawable.at_1_3,R.drawable.at_1_4,
                    R.drawable.at_1_5,
                ), 2, 2),
                Hall(1, "اتيليه 2", mutableListOf<Int>(
                    R.drawable.at_2_1, R.drawable.at_2_2,R.drawable.at_2_3,
                ), 2, 2),
                Hall(2, "اتيليه ٤", mutableListOf<Int>(
                    R.drawable.at_4_1, R.drawable.at_4_2,R.drawable.at_4_3,
                ), 2, 2),
                Hall(3, "قاعة امين عبدالله", mutableListOf<Int>(
                    R.drawable.hall_mohamed_1, R.drawable.hall_mohamed_2,R.drawable.hall_mohamed_3,
                    R.drawable.hall_mohamed_4,
                ), 2, 2),
                Hall(4, "قاعه تكنولوجيا", mutableListOf<Int>(
                    R.drawable.hall_tech_1, R.drawable.hall_tech_2,R.drawable.hall_tech_3,R.drawable.hall_tech_4,
                ), 2, 2),
                Hall(5, "معمل الميكروبيولوجي", mutableListOf<Int>(
                    R.drawable.hall_micro_1, R.drawable.hall_micro_2,R.drawable.hall_micro_3,
                    R.drawable.hall_micro_4,
                ), 2, 2)
            ), 2),

        )
    ),
    Building(
        3,
        "مسرح طلعت حرب",
        "Theater",
        R.drawable.theater_thumb,
        null,
        mutableListOf<Floor>()
    ),
)