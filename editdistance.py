# Given two strings word1 and word2, return
# the minimum number of operations required
# to convert word1 to word2.

# You have the following three operations permitted on a word:

# Insert a character
# Delete a character
# Replace a character
import random
import string
import time

from Levenshtein import distance


def editdistance(s1: str, s2: str):
    if len(s1) == 0:
        return len(s2)

    if len(s2) == 0:
        return len(s1)

    c = 0 if s1[0] == s2[0] else 1

    return min(
        editdistance(s1[1:], s2) + 1,
        editdistance(s1, s2[1:]) + 1,
        editdistance(s1[1:], s2[1:]) + c
    )


def editdistance_memoized(s1: str, s2: str):
    calls = [[None for _ in range(len(s2) + 1)] for _ in range(len(s1) + 1)]

    def go(s1: str, i1: int, s2: str, i2: int):
        if calls[i1][i2] != None:
            return calls[i1][i2]

        if len(s1) == i1:
            calls[i1][i2] = len(s2) - i2
            return len(s2) - i2

        if len(s2) == i2:
            calls[i1][i2] = len(s1) - i1
            return len(s1) - i1

        c = 0 if s1[i1] == s2[i2] else 1

        result = min(
            go(s1, i1 + 1, s2, i2) + 1,
            go(s1, i1, s2, i2 + 1) + 1,
            go(s1, i1 + 1, s2, i2 + 1) + c
        )
        calls[i1][i2] = result
        return result

    res = go(s1, 0, s2, 0)
    for c in calls:
        print(c)
    return res


def editdistance_tabularized(s1: str, s2: str):
    calls = [[0 for _ in range(len(s2) + 1)] for _ in range(len(s1) + 1)]

    print(s1, s2)
    for i in range(len(s1) - 1, -1, -1):
        for j in range(len(s2) - 1, -1, -1):
            if i == len(s1):
                calls[i][j] = len(s2) - 1 - j
                continue

            if j == len(s2):
                calls[i][j] = len(s1) - 1 - i
                continue

            c = 0 if s1[i] == s2[j] else 1

            calls[i][j] = min(
                calls[i + 1][j] + 1,
                calls[i][j + 1] + 1,
                calls[i + 1][j + 1] + c
            )
    for c in calls:
        print(c)
    return calls[0][0]


def genStr(n: int):
    return ''.join(random.choices(string.ascii_lowercase, k=n))


def mutStr(s: str):
    for i in range(len(s)//10):
        s = s.replace(s[i*10:i*10+10], random.choices(
            [
                ''.join(random.choices(string.ascii_lowercase, k=10)),
                "",
                s[i*10:i*10+10]
            ], k=1)[0])

    return s


# print(editdistance('horse', 'ros')) # 3

s1 = "a" * 2
s2 = "a" * 2 + "b" * 2
print(distance(s1, s2))  # 5
print(editdistance(s1, s2))  # 5
print(editdistance_memoized(s1, s2))  # 5
print(editdistance_tabularized(s1, s2))  # 5

exit(0)
for i in range(100):
    s1 = genStr(20)
    s2 = mutStr(s1)

    t1 = time.time()
    edl = distance(s1, s2)
    t2 = time.time()
    edm = editdistance_memoized(s1, s2)  # 5
    t3 = time.time()
    edt = editdistance_tabularized(s1, s2)  # 5
    t4 = time.time()
    # edn = editdistance(s1, s2) # 5
    # t5 = time.time()

    if edt != edl:
        print(s1)
        print(s2)
        print(edl)
        print(edm)
        print(edt)
        print("Error")
        break

    print("Library:", t2 - t1)
    print("Memoized:", t3 - t2)
    print("Tabularized:", t4 - t3)


# print(editdistance('a', 'b')) # 1
# print(editdistance('a', 'a')) # 0
# print(editdistance('a', '')) # 1
# print(editdistance('kitten', 'sitting')) # 3
