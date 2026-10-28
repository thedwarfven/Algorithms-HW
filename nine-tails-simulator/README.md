# Relational Nine Tails Simulator

A standalone interactive 3-by-3 coin puzzle with a shortest-path BFS solver.

## Rules

Click any coin, heads or tails. The selected coin and its up, down, left, and right neighbors flip. Diagonal coins do not flip. The goal is nine tails.

## Features

- Editable starting boards and random puzzles
- Undo, restart, and hints
- Shortest solutions using breadth-first search across 512 states
- Step-by-step solution playback and state inspection

## Run locally

Download index.html and open it in a browser. No installation or dependencies are required.

## GitHub Pages

In repository Settings > Pages, choose Deploy from a branch, select master and / (root), then save. If Pages already hosts other repository content, keep that configuration and ensure it publishes this folder.

With master and the repository root published, the simulator URL is:
https://thedwarfven.github.io/Algorithms-HW/nine-tails-simulator/

The URL works only after GitHub Pages is enabled and deployment completes.
