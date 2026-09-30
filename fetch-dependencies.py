#!/usr/bin/env python3
"""Fetch exact, unmodified upstream build dependencies; verify before installing."""
import hashlib
import json
from pathlib import Path
from urllib.request import Request, urlopen

root = Path(__file__).resolve().parent
target = root / 'libs'
target.mkdir(exist_ok=True)
for dependency in json.loads((root / 'dependencies.lock.json').read_text()):
    if dependency['usage'] != 'build-and-runtime':
        continue  # Polymer's build dependency is resolved through its official Maven repository.
    path = target / dependency['filename']
    if path.exists():
        data = path.read_bytes()
    else:
        with urlopen(Request(dependency['url'], headers={'User-Agent': 'MapstitchPolymerCompat/1.0'}), timeout=60) as response:
            data = response.read()
    if hashlib.sha512(data).hexdigest() != dependency['sha512']:
        raise SystemExit(f'Hash mismatch; refusing to use or overwrite {path}')
    if not path.exists():
        path.write_bytes(data)
    print(f'Verified {path.name}')
