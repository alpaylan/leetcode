# Given an input string (s) and a pattern (p), implement
# wildcard pattern matching with support for '?' and '*' where:

# '?' Matches any single character.
# '*' Matches any sequence of characters (including the empty sequence).
# The matching should cover the entire input string (not partial).

call_counts = {}
calls = {}

def isMatch(s: str, p: str) -> bool:
    # Call counter
    if (s, p) in call_counts:
        call_counts[(s, p)] += 1
    else:
        call_counts[(s, p)] = 1

    # Memoization
    if (s, p) in calls:
        return calls[(s, p)]

    ## Base cases

    # When both are consumed, it's a match
    if len(s) == 0 and len(p) == 0:
        calls[(s, p)] = True
        return True
    
    # When pattern is consumed, it's not a match
    if len(p) == 0:
        calls[(s, p)] = False
        return False
    
    # When string is consumed, it's a match if the rest of the pattern is all '*'
    if len(s) == 0:
        return p[0] == '*' and isMatch(s, p[1:])
    
    ## Recursive cases

    # Consume both if they match
    # a___ <> a___
    if s[0] == p[0]:
        return isMatch(s[1:], p[1:])
    
    # Consume both if pattern is '?'
    # a___ <> ?___
    if p[0] == '?':
        return isMatch(s[1:], p[1:])
    
    # There are 3 cases for '*'
    # a___ <> *___
    if p[0] == '*':
        # 1. Consume both: so that '*' matches 1 character
        c1 = isMatch(s[1:], p[1:])
        # 2. Consume string, but not pattern: so that '*' matches 1 or more characters
        c2 = isMatch(s[1:], p)
        # 3. Consume pattern, but not string: so that '*' matches 0 characters
        c3 = isMatch(s, p[1:])
        return c1 or c2 or c3

    calls[(s, p)] = False
    return False

# print(isMatch('aa', 'a')) # False
# print(isMatch('aa', '*')) # True
# print(isMatch('cb', '?a')) # False
# print(isMatch('adceb', '*a*b')) # True
# print(isMatch('acdcb', 'a*c?b')) # False
# print(isMatch('adceb', '*a*b*')) # True


for i in range(1, 6):
    for j in range(1, 6):
        isMatch(i*'a', j*'*')
        maxelem = sorted(call_counts)[0]
        allcalls = sum(call_counts.values())
        print(f"isMatch({i*'a'}, {j*'*'}): {call_counts[maxelem]}, {allcalls}")
        call_counts = {}
        calls = {}