## Пример формирования и подписания запроса на регистрацию пользователя в УЦ КриптоПро.

Этот пример предназначен для того, чтобы продемонстрировать, как с использованием криптографического контейнера PFX можно подписывать запрос на регистрацию в УЦ КриптоПро. Мы будем использовать библиотеку JavaCSP и средства КриптоПро CSP для работы с контейнером, а также для подписания и формирования запроса.

## Требования:

- Java 21 или более поздняя версия.
- КриптоПро CSP или JavaCSP.
- Установленные библиотеки и компоненты для работы с PKCS#12 (PFX).
- Наличие действующего контейнера PFX с сертификатом.

## Установка:

Для использования примера, вам нужно установить КриптоПро CSP и JavaCSP, а также добавить их в ваш проект. Для этого:

- Для скачивания библиотек потребуется регистрация на сайте [КриптоПро](https://cryptopro.ru/).
- Скачайте и установите [КриптоПро CSP](https://cryptopro.ru/products/csp/jcsp) (он отвечает за создание и шифрование запроса в формате Base64 PKCS#7).
- Установите [JavaCSP](https://cryptopro.ru/products/csp/jcsp), который позволяет взаимодействовать с КриптоПро CSP из Java-кода (файлы .jar нужно поместить в папку **_libs_**).
- Установите [Bouncy Castle](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk18on) (можно использовать версии, которые идут в комплекте с JavaCSP, так как они проверены на совместимость с данной версией провайдера).
- Добавить файл PFX в **_resources/config/certificates_**
- В файле **_application.yml_** заполните следующие поля (их можно передавать через переменные перед запуском программы):
```yaml
    path: ${pfx_path:"ПОДСТАВЬ ЗНАЧЕНИЕ"}  # относительный путь к файлу
    alias: ${pfx_alias:"ПОДСТАВЬ ЗНАЧЕНИЕ"}  # алиас контейнера
    password: ${pfx_password:"ПОДСТАВЬ ЗНАЧЕНИЕ"}  # пароль к контейнеру
```

## Пример использования:
После запуска проекта откройте терминал и выполните команду для создания запроса:

- Привилегированное подписание запроса:
```curl
    curl http://localhost:8080/api/v1/sign 
```
- "Двойное" подписание запроса:
```curl
    curl http://localhost:8080/api/v2/sign 
```

## Пример ответа:
```text
    MIAGCSqGSIb3DQEHAqCAMIACAQExDDAKBggqhQMHAQECAjCABgkqhkiG9w0BBwGggCSABIIBQjxQ
    cm9maWxlQXR0cmlidXRlc0NoYW5nZT48VG8+PEF0dHJpYnV0ZSBPaWQ9IjIuNS40LjMiIFZhbHVl
    PSJEdW1teUNOIi8+PEF0dHJpYnV0ZSBPaWQ9IjIuNS40LjYiIFZhbHVlPSJSVSIvPjxBdHRyaWJ1
    dGUgT2lkPSIyLjUuNC43IiBWYWx1ZT0iTW9zY293Ii8+PEF0dHJpYnV0ZSBPaWQ9IjIuNS40Ljgi
    IFZhbHVlPSJNU0siLz48QXR0cmlidXRlIE9pZD0iMi41LjQuOSIgVmFsdWU9IkxlbmluYSwgMSIv
    PjxBdHRyaWJ1dGUgT2lkPSIxLjIuODQwLjExMzU0OS4xLjkuMSIgVmFsdWU9InRlc3RAdGVzdC5y
    dSIvPjwvVG8+PC9Qcm9maWxlQXR0cmlidXRlc0NoYW5nZT4AAAAAAACggDCCCtcwggqEoAMCAQIC
    EQdJCcMARLN+uEA2Zzc1fcwWMAoGCCqFAwcBAQMCMIIBCzEYMBYGBSqFA2QBEg0xMDI3NzAwMDY3
    MzI4MRowGAYIKoUDA4EDAQESDDAwNzcyODE2ODk3MTELMAkGA1UEBhMCUlUxGDAWBgNVBAgMDzc3
    INCc0L7RgdC60LLQsDEVMBMGA1UEBwwM0JzQvtGB0LrQstCwMSowKAYDVQQJDCHQo9C7LiDQmtCw
    0LvQsNC90YfQtdCy0YHQutCw0Y8gMjcxDzANBgNVBAsMBtCj0JjQkTEhMB8GA1UECgwY0JDQniDQ
                             // some other lines //
    A1UEBhMCUlUxGDAWBgNVBAgMDzc3INCc0L7RgdC60LLQsDEVMBMGA1UEBwwM0JzQvtGB0LrQstCw
    MSowKAYDVQQJDCHQo9C7LiDQmtCw0LvQsNC90YfQtdCy0YHQutCw0Y8gMjcxDzANBgNVBAsMBtCj
    0JjQkTEhMB8GA1UECgwY0JDQniDQkNCb0KzQpNCQLdCR0JDQndCaMTUwMwYDVQQDDCzQotCV0KHQ
    oiDQo9CmIDIuMCDQkNCeICLQkNCb0KzQpNCQLdCR0JDQndCaIgIRB0kJwwBEs364QDZnNzV9zBYw
    CgYIKoUDBwEBAQEEQP0F/zZUGxQGNM1cdlaBype5acWUwAmY90MpW8r85kkDCICU99tyjGs0pqbs
    JcikLHTOprU7f9ZnKGZYhkqDNF8AAAAAAAA=
```