
def two_sums(arr: list[int]) -> dict[int, tuple[int, int]]:
    """
    Returns a dictionary of all the two sums in the array
    """
    two_sum_dict = {}
    for i in range(len(arr)):
        for j in range(i + 1, len(arr)):
            if arr[i] + arr[j] not in two_sum_dict:
                two_sum_dict[arr[i] + arr[j]] = [(arr[i], arr[j])]
            else:
                two_sum_dict[arr[i] + arr[j]].append((arr[i], arr[j]))
    return two_sum_dict


def three_sum(arr: list[int], sum: int) -> list[int]:
    if len(arr) < 3:
        return []

    c1 = three_sum(arr[:-1], sum)
    
    two_sum = two_sums(arr[:-1])

    c2 = list(map(lambda t: (t[0], t[1], arr[-1]), two_sum.get(sum - arr[-1], [])))

    return c1 + c2

def three_sum_cached(arr: list[int], sum: int, two_sums: dict[int, tuple[int, int]]) -> list[int]:
    if len(arr) < 3:
        return []

    c1 = three_sum_cached(arr[:-1], sum, two_sums)

    matching_two_sums = two_sums.get(sum - arr[-1], [])

    # This is to prevent using the same number twice
    filtered_two_sums = list(filter(lambda t: t[0] != arr[-1] and t[1] != arr[-1], matching_two_sums))

    c2 = list(map(lambda t: (t[0], t[1], arr[-1]), filtered_two_sums))
    filtered_c2 = list(filter(lambda t: t[0] < t[1] < t[2], c2))

    return c1 + filtered_c2




if __name__ == "__main__":
    arr = list(range(1, 100))
    sum = 80
    

    for i in range(10, 100):
        print(i)
        for j in range(i + 1, i*2):
            arr = list(range(1, i))
            two_sums_cached = two_sums(arr)
            # r1 = three_sum(arr, j)
            r2 = three_sum_cached(arr, j, two_sums_cached)
            # assert r1 == r2
