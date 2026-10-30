# 16 Tails Simulator

A standalone 4x4 version of the relational Nine Tails simulator.

Click any coin, heads or tails. The selected coin and its horizontal and vertical neighbors flip. Diagonal coins do not flip. The goal is sixteen tails.

## Controls

- New puzzle generates a solvable starting board.
- Edit starting board toggles individual coins.
- Load board accepts sixteen H/T letters in row order.
- Undo, Restart, and Hint support manual play.
- Find shortest solution, Next step, Auto-play, and solution tiles show a minimum-move solution.

## Solver

The graph contains 65,536 possible boards. BFS from the all-tails goal reaches 4,096 of them. Custom boards outside that component report no solution. The solver uses typed arrays and generates neighbors directly, without storing the entire graph.

All 65,536 distances were checked against exhaustive enumeration of the 65,536 possible click subsets. Interaction checks covered tails clicks, flip reversal, editing, undo, loading boards, random solvable puzzles, playback, and rewind.

## Open

Download index.html and open it in a browser. No installation or dependencies are required.

When GitHub Pages publishes master from the repository root:
https://thedwarfven.github.io/Algorithms-HW/sixteen-tails-simulator/

If Pages is not enabled, open repository Settings > Pages and select Deploy from a branch, master, / (root), then Save.
