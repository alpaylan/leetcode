class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []


from typing import Optional


class Solution:
    def cloneGraph(self, node: Optional["Node"]) -> Optional["Node"]:
        cache = {}

        def cloneGraph_(node: Optional["Node"]) -> Optional["Node"]:
            if node is None:
                return None
            if node.val in cache:
                return cache[node.val]
            else:
                clone = Node(node.val)
                cache[node.val] = clone
                for neighbor in node.neighbors:
                    clone.neighbors.append(cloneGraph_(neighbor))
                return clone


        match node:
            case None:
                return None
            case Node:
                
                neighbors = list(
                    filter(
                        lambda k: k is not None,
                        map(lambda n_: self.cloneGraph(n_), node.neighbors),
                    )
                )
                return Node(node.val, neighbors)


nodes = [Node(i) for i in range(4)]
nodes[0].neighbors = [nodes[1], nodes[3]]
nodes[1].neighbors = [nodes[0], nodes[2]]
nodes[2].neighbors = [nodes[1], nodes[3]]
nodes[3].neighbors = [nodes[0], nodes[2]]

s = Solution()
clone = s.cloneGraph(nodes[0])
print(clone.val)
