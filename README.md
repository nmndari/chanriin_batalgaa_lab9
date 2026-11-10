# Лаб 9 — Код хамралт — JaCoCo-гоор тестээ хөтлөх

- **Оюутны нэр:** Б. Намуундарь
- **Оюутны код:** B232270037

## Орчин

JUnit 5.10.2, JaCoCo 0.8.12 (`pom.xml`-д заасан).

`java -version`:

```
openjdk version "17.0.17" 2025-10-21
OpenJDK Runtime Environment Homebrew (build 17.0.17+0)
OpenJDK 64-Bit Server VM Homebrew (build 17.0.17+0, mixed mode, sharing)
```

`mvn -version`:

```
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 17.0.17, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk@17/17.0.17/libexec/openjdk.jdk/Contents/Home
Default locale: en_MN, platform encoding: UTF-8
OS name: "mac os x", version: "15.6", arch: "aarch64", family: "mac"
```

## Хэсэг A: Happy path тестүүд ба эхний хэмжилт

Зөвхөн хэвийн урсгалын 5 тест бичсэн (`Tests run: 5`, бүгд ногоон).
Гаралт: [results/mvn-verify-A.txt](results/mvn-verify-A.txt), [results/jacoco-A.csv](results/jacoco-A.csv).

| Хэмжүүр | Covered | Missed | Хамралт |
|---|---|---|---|
| Line | 15 | 10 | 15/25 = **60.0%** |
| Branch | 20 | 21 | 20/41 ≈ **48.8%** |

Шар ромботой мөрүүд нь оролтын шалгалтын `||` нөхцөлүүд (20, 31, 48-р мөр — зөвхөн "бүгд худал" талаараа явсан), `gpa`-ийн `switch` (36 — 5 салаанаас зөвхөн A, B), мөн `scholarship`-ийн `hasViolation` ба `&&` нөхцөлүүд (51–53 — зөрчилтэй болон "голч хангасан ч кредит дутуу" тохиолдол туршигдаагүй).
Улаан мөрүүд нь гурван `throw new IllegalArgumentException` (21, 32, 49) буюу буруу оролтын алдааны замууд юм.
Үүнээс гадна F дүн буцаах (27), `gpa`-ийн C, D case (39, 40), тэтгэлэггүй `return 0` (55) мөрүүд огт ажиллаагүй.

## Хэсэг B: CFG ба branch 100%

### `scholarship()` функцын CFG

CFG-г draw.io ашиглан зурсан; ирмэг бүр дээр түүнийг дайрч буй тестийн нэрийг бичсэн.

![scholarship() функцын CFG](cfg.png)

| Ирмэг | Дайрч буй тест | Хэсэг |
|---|---|---|
| 1 –T→ 4 | `scholarship_negativeGpa_throws` | B (шинэ) |
| 2 –T→ 4 | `scholarship_gpaAbove4_throws` | B (шинэ) |
| 3 –T→ 4 | `scholarship_negativeCredits_throws` | B (шинэ) |
| 1 –F→ 2 –F→ 3 –F→ 5 | `scholarship_gpa39_credits16_noViolation_returns100` | A |
| 5 –T→ 6 | `scholarship_hasViolation_returns0` | B (шинэ) |
| 5 –F→ 7 –T→ 8 –T→ 9 | `scholarship_gpa39_credits16_noViolation_returns100` | A |
| 8 –F→ 10 | `scholarship_gpa39_credits14_returns70` | B (шинэ) |
| 7 –F→ 10 –T→ 11 –T→ 12 | `scholarship_gpa35_credits12_noViolation_returns70` | A |
| 11 –F→ 13 | `scholarship_gpa35_credits10_returns50` | B (шинэ) |
| 10 –F→ 13 –T→ 14 | `scholarship_gpa32_credits16_returns50` | B (шинэ) |
| 13 –F→ 15 | `scholarship_gpa25_credits16_returns0` | B (шинэ) |

### Нэмсэн тестүүд ба үр дүн

Хэсэг A-гийн 5 тест дээр **16 тест нэмж**, нийт **21 тест** болсон (`Tests run: 21, Failures: 0`):

- `letterGrade` — 5 шинэ: сөрөг оноо, 100-аас их оноо (`assertThrows`), C, D, F дүн.
- `gpa` — 3 шинэ: `null`, хоосон массив (`assertThrows`), таван дүн бүгдийг агуулсан массив (C, D case ба `default` салаа).
- `scholarship` — 8 шинэ: `||` нөхцөл тус бүрд нэг `assertThrows` (3), зөрчилтэй, голч хангасан ч кредит дутуу хоёр тохиолдол, 50%, 0%.

Гаралт: [results/mvn-verify-B.txt](results/mvn-verify-B.txt), [results/jacoco-B.csv](results/jacoco-B.csv).
`results/jacoco-B.csv`-д `BRANCH_MISSED = 0`, `LINE_MISSED = 0` (`BRANCH_COVERED = 41`, `LINE_COVERED = 25`).

| Хэмжүүр | Covered | Missed | Хамралт |
|---|---|---|---|
| Line | 25 | 0 | 25/25 = **100%** |
| Branch | 41 | 0 | 41/41 = **100%** |

## Хэсэг C: Давталтын заагийн тестүүд

`gpa()` доторх давталтад 0 / 1 / 2+ дүрмийг хэрэгжүүлсэн (нийт **23 тест**, `Tests run: 23, Failures: 0`):

| Давталт | Тест | Шалгасан зүйл |
|---|---|---|
| 0 удаа | `gpa_emptyArray_throws` | `gpa([])` → `IllegalArgumentException` |
| 0 удаа | `gpa_nullArray_throws` | `gpa(null)` → `IllegalArgumentException` |
| 1 удаа | `gpa_loopOnce_singleScore95_returns4point0` | `gpa([95]) == 4.0` |
| 2+ удаа | `gpa_loopManyTimes_fourScores_returns2point5` | `gpa([95, 85, 75, 65]) == 2.5` |

Хамралт 100% хэвээр ([results/jacoco-C.csv](results/jacoco-C.csv)), гэхдээ хамралт нь мөр, салаа "нэг удаа ч гэсэн ажилласан уу" гэдгийг л хэмждэг болохоос давталт хэдэн удаа эргэсэн, үр дүн нь зөв эсэхийг хэлдэггүй.
Давталтын алдаанууд (хоосон оролтод тэгд хуваах, эхний эсвэл сүүлийн элементийг алгасах, нийлбэрийг буруу хуримтлуулах) яг 0, 1, олон удаагийн зааг дээр л илэрдэг тул эдгээр тест хамралтын тоог өсгөхгүй ч хэрэгтэй хэвээр.

## Хэсэг D: Хамралт 100% — гэхдээ алдаа амьд

`letterGrade()`-д 7 зааг утгын тест нэмсэн (0, 59.99, 60, 69.99, 89.99, 90, 100), нийт **30 тест**.

**Унасан тест** ([results/mvn-test-D-fail.txt](results/mvn-test-D-fail.txt), `Tests run: 30, Failures: 1`):

```
ScholarshipCalculatorTest.letterGrade_boundary60_returnsD:160 expected: <D> but was: <F>
```

**Алдаа.** Спецификацид "60-69.99 -> D" гэсэн атал кодод `if (score > 60) return "D";` гэж бичигдсэн байсан тул яг 60 оноо F болж байв.
Засвар: `score > 60` → `score >= 60` (`ScholarshipCalculator.java`, 26-р мөр).
Засварыг тусдаа commit-оор хийсэн: `Fix letterGrade: > 60 -> >= 60, spec says 60 is D`.

**Засварын дараа** ([results/mvn-verify-final.txt](results/mvn-verify-final.txt), [results/jacoco-final.csv](results/jacoco-final.csv)): `Tests run: 30, Failures: 0`, `BUILD SUCCESS`.

| Хэмжүүр | Covered | Missed | Хамралт |
|---|---|---|---|
| Line | 25 | 0 | 25/25 = **100%** |
| Branch | 41 | 0 | 41/41 = **100%** |

### Гол дүгнэлт

Хэсэг B-ийн төгсгөлд branch хамралт 41/41 = 100% байсан ч `letterGrade(60)` буруу хариу өгдөг алдаа кодод амьд үлдсэн байв.
Учир нь `score > 60` ба `score >= 60` хоёул яг ижил хоёр салааг (үнэн, худал) үүсгэдэг: 65 оноо үнэн салаагаар, 50 оноо худал салаагаар явахад JaCoCo хоёуланг нь "хамрагдсан" гэж тоолно. Хоёр операторын ялгаа зөвхөн ганц утга дээр — яг 60 дээр — гардаг бөгөөд тэр утгыг шалгасан тест байхгүй үед алдаа "үл үзэгдэгч" байсан.
Хамралт нь кодын аль мөр, аль салаа тестийн үеэр ажилласныг хэмждэг ба тэр салаа зөв нөхцөлөөр сонгогдсон эсэх, буцаасан утга спецификацитай таарч байгаа эсэх, бүх чухал оролт шалгагдсан эсэхийг хэмждэггүй. 

Тиймээс хамралт бол "тест хүрээгүй код хаана байна" гэдгийг харуулах зайлшгүй хэрэгсэл боловч зөв байдлын баталгаа биш — түүнийг спецификацид суурилсан зааг утгын тестүүдтэй хамт хэрэглэж байж алдааг барина.

### AI ашиглалтын тэмдэглэл
Уг лабораторийн хүрээнд AI туслахыг дараах байдлаар ашиглав.
1. Тайлангийн формат засаж, харагдах байдлыг сайжруулахад хэрэглэв.
2. Зааг утгын алдаа олдож зассан засвараа тусад нь commit хийхээ мартсан байсан тул AI-аас хэрхэн шийдэхээ асуув. Түүний дагуу засварлан тохирох commit-үүдийг хийсэн.
