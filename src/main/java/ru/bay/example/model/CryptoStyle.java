package ru.bay.example.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum CryptoStyle {
    CN("2.5.4.3"),
    O("2.5.4.10"),
    INNLE("1.2.643.100.4"),
    C("2.5.4.6"),
    L("2.5.4.7"),
    S("2.5.4.8"),
    STREET("2.5.4.9"),
    SN("2.5.4.4"),
    G("2.5.4.42"),
    SNILS("1.2.643.100.3"),
    T("2.5.4.12"),
    E("1.2.840.113549.1.9.1");

    private final String oid;
}

// CN    - 2.5.4.3 - Вагнер Тамара Олеговна
// O     - 2.5.4.10 - "OOO Big Boss"
// INNLE - 1.2.643.100.4 - 500094505819
// C     - 2.5.4.6 - RU
// L     - 2.5.4.7 - Москва
// S     - 2.5.4.8 - Республика Адыгея
// STREET- 2.5.4.9 - Москва, Республика Адыгея, Краснобогатырская, 73"
// SN    - 2.5.4.4 - "Вагнер"
// G     - 2.5.4.42 - "Тамара"
// SNILS - 1.2.643.100.3 - 15231801221
// T     - 2.5.4.12 - "Tough Guy" (Должность)
// E     - 1.2.840.113549.1.9.1 - "User641@pochtochka.com"