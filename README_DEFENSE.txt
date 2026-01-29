README_DEFENSE (Quick talking points)

1) Why custom Data Structures?
   - The PDF forbids java.util structures for DS parts, so we implemented them manually.
   - Each DS is not فقط «کلاس»، بلکه در منطق واقعی بازی استفاده می‌شود.

2) Where is each DS used?
   - HashTable: server/GameServer clients registry (replaces HashMap) + fast lookups
   - Stack: Undo/Redo actions in server/GameState
   - Queue: FIFO processing of client requests (server-side)
   - Graph (Adjacency List): records player-to-player money transfers (rent, trades) with cumulative weights
   - BST: property reports (<= price) + BST supports insert/delete/inorder
   - Heap: Top-K richest players; updateKeyByPlayerId implemented

3) UI rules
   - Swing + LayoutManagers (no absolute layout)
   - FlatLaf enabled for modern look
   - BoardPanel draws board, tokens, color groups, mortgage, houses/hotel markers

Run:
   - Run server.GameServer
   - Run client.SwingGameClient (multiple times for multiple players)
\n\nMenu UI:\n- Run Game.main to open the menu (Start Server / Start Client).\n
Menu upgrades:
- Settings: host/port + number of clients to launch.
- Start Client launches N windows.

Menu extra:
- Player Setup: set names + token colors for clients.
- Start Local Game: starts server then launches clients.
