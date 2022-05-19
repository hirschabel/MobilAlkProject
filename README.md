# MobilAlkProject

- Firebase autentikáció:
  - Bejelentkezés: MainActivity.java (45. sor)
  - Regisztráció: RegisterActivity.java (52. sor)

- Adatmodell:
  - AruItem

- Layoutok:
  - ConstraintLayout: (src/main/res/layout/activity_arukereso.xml)
  - LinearLayout: (src/main/res/layout-land/activity_main.xml)

- Animációk:
  - NINCS

- Lifecycle hook:
  - onDestroy(): ArukeresoActivity.java (273. sor)

- Android permission használata:
  - NINCS

- CRUD:
  - Create: RegisterActivity.java (52. sor)
  - Read: ArukeresoActivity.java (143. sor)
  - Update: PasswordChangeActivity.java (64. sor)
  - Delete: ArukeresoActivity.java (243. sor)

- 2 komplex Firestore lekérdezés:
  - orderBy(ASC) / Limit: ArukeresoActivity.java (143. sor)
  - orderBy(DESC) / Limit: ArukeresoActivity.java (163. sor)

2021/22/2
