> **Archived on 2026-10-04.** Future combined Minecraft 26.3 development continues in [Vanilla++ Quality of Life Suite](https://github.com/THENATHE/vanilla-plusplus-quality-of-life-suite). Existing standalone releases and source remain available here.
>
> The suite incorporates this component. Existing standalone installations remain a separate option; follow the suite installation instructions when migrating.

# MapStitch Polymer Shim

**Let vanilla and MapStitch players share one Minecraft server while preserving the native MapStitch experience.**

MapStitch Polymer Shim is a separate, server-only Fabric compatibility layer for **Minecraft Java Edition 26.3**, released under the mod name **MapStitch Polymer Compatibility**. Players with the original MapStitch client keep their atlas, minimap, world map, crafting, and map data. Players without MapStitch can join and see safe atlas placeholders, with a clear notice that using the atlas requires the client mod.

The shim works with the original MapStitch and Polymer releases. Their JARs are neither modified nor bundled. Current shim version: **1.0.1+26.3**.

[Download releases](https://github.com/THENATHE/mapstitch-polymer-shim/releases) · [Validation](VALIDATION.md) · [Shared Region Maps Shim](https://github.com/THENATHE/shared-region-maps-shim)

## Features

- **Mixed client support:** vanilla, MapStitch, and MapStitch-with-Polymer clients can connect to the same dedicated server.
- **Native MapStitch behavior:** clients with MapStitch receive the original atlas item, custom components, recipe information, and supported custom packets. Atlas contents, scale, selected map, ejection preference, and map-center metadata retain their native representation.
- **Safe vanilla display:** an atlas appears as a book named **Atlas (MapStitch)** while remaining a real atlas in the server's inventory and saved data. MapStitch-only components are filtered from packets sent to clients that cannot decode them, including components on ordinary filled maps.
- **Clear client requirement:** holding an atlas in either hand without MapStitch displays **Install MapStitch on your client to use this atlas.** The notice appears on equip and repeats every 200 server ticks, approximately ten seconds. Native MapStitch clients do not receive it.
- **Optional original artwork:** a Polymer resource pack can display MapStitch's original atlas artwork and all five fullness states for vanilla clients. Players who decline the pack keep the book appearance.
- **Per-client networking:** unsupported MapStitch packets are withheld, and registry IDs and tags are translated consistently for native clients.
- **Preserved server data:** the original atlas identity and saved components remain intact; the placeholder is a client-facing representation.
- **No shim configuration required:** install the server JAR and its dependencies. Optional artwork hosting uses Polymer's existing configuration.

Vanilla support is **display-only**. It does not provide the atlas interface, minimap, or world-map screen to players without MapStitch, even when they accept the resource pack.

## Direct compatibility layers

| Project | Integration supplied by this shim |
|---|---|
| **MapStitch** | Attaches an overlay to the existing atlas, preserves native items and components for supported clients, and prevents unsupported custom packets from reaching other clients. |
| **Polymer Core and Registry Sync Manipulator** | Supplies the vanilla item fallback and component filtering, restores MapStitch's native registry entries per connection, and coordinates native item/component IDs with Polymer. |
| **Polymer Resource Pack** | Generates vanilla-compatible atlas models using the installed MapStitch artwork and selects those models only for players who have accepted the pack. |
| **Fabric API** | Uses Fabric's existing channel negotiation and registry synchronization to detect native MapStitch support before preparing the connection. Also supplies the lifecycle and networking hooks. |

**[Shared Region Maps Shim](https://github.com/THENATHE/shared-region-maps-shim)** is an optional companion, not a dependency or a dedicated integration implemented by this shim. Its own MapStitch layer supplies regional sharing for newly generated atlas maps and map-center repairs. With Shared Region Maps **1.0.3+mc26.3**, ordinary automatic atlas maps and ordinary maps can reuse records for the same region, dimension, and scale. Its repairs cover missing centers in carried atlases and center refreshes during insertion/scaling; existing independent records are preserved without retroactive merging. The Polymer shim remains responsible for client compatibility.

Fabric Language Kotlin and Fzzy Config are upstream runtime dependencies, not additional mods that this shim converts for vanilla clients. This project targets **MapStitch**; it is not a compatibility shim for the separate **Map Atlases** mod.

## Install

Install `mapstitch-polymer-compat-1.0.1+26.3.jar` in the **dedicated server's** `mods` directory alongside the dependencies below, then restart.

| Dependency | Tested version |
|---|---|
| Minecraft Java Edition | 26.3 |
| Java | 25 |
| Fabric Loader | 0.19.5 |
| Fabric API | 0.161.0+26.3 |
| [MapStitch](https://modrinth.com/mod/mapstitch/version/kRE6nsB7) | 1.1.6+26.3 |
| [Polymer Bundled](https://modrinth.com/mod/polymer/version/REBDssAz) | 0.18.2+26.3 |
| [Fzzy Config](https://modrinth.com/mod/fzzy-config/version/thw1Z19c) | 0.7.7+fix2+26.3 |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin/version/eRRZzGMc) | 1.14.1+kotlin.2.4.20 |

The shim explicitly pins MapStitch's internal mod version `1.1.6` and Polymer Core/Resource Pack `0.18.2+26.3`. The MapStitch release filename includes the Minecraft suffix. Use the tested releases together; upstream updates can change mixin targets or protocols and require a shim update.

| Player's client | Client installation | Result |
|---|---|---|
| Vanilla | None | Can join; display-only atlas placeholder and client-mod-required notice. |
| MapStitch | Original MapStitch and its normal dependencies | Native atlas, minimap, and world-map features. |
| MapStitch + Polymer | Original MapStitch, its normal dependencies, and Polymer | Native MapStitch features, subject to the Polymer client cache limitation below. |

**Do not install this compatibility JAR on clients.** A resource pack is optional and is not required to connect.

## How it works

The shim chooses the representation for each connection. It uses Fabric's existing advertisement of supported play channels to identify an unmodified MapStitch client before registry synchronization. That client receives the native atlas and MapStitch components; other clients receive Polymer's book-based representation with unsupported data filtered out. The server continues to store the real items and components.

For native connections, the shim restores MapStitch's item, data-component, and recipe-serializer entries and assigns a compact set of connection-specific numeric IDs. Packet encoding, decoding, and tag synchronization use the same mapping. This allows native MapStitch data and Polymer's filtered registries to coexist without rewriting world saves or requiring a custom client handshake.

When a native MapStitch client also runs Polymer, the shim suppresses Polymer's separate enhanced **item and component registry caches** for that connection so they cannot replace Fabric's negotiated IDs. Other Polymer synchronization continues. Enhanced metadata and virtual items belonging to other Polymer mods on those particular clients are outside the supported scope. Vanilla clients and Polymer clients without MapStitch retain normal Polymer behavior.

## Optional atlas artwork

The shim registers a resource-pack generator with Polymer. It reads five original atlas PNGs from the installed MapStitch JAR and creates vanilla-compatible item definitions in the shim's own namespace. Native MapStitch models remain unchanged. The generated pack includes the upstream artwork's MIT license notice and credits.

To offer the artwork using Polymer Bundled's built-in hosting:

1. Stop the server.
2. In `config/polymer/auto-host.json`, set `"enabled": true` and `"required": false`, preserving the other settings.
3. Restart. Polymer generates and offers `polymer/resource_pack.zip`.

Players can accept the pack for the atlas artwork or decline it for the book appearance. The client-mod-required notice remains active in either case. If hosting was already enabled, restart after installing the shim to regenerate the pack.

Use the pack generated by your own server so it also contains your other Polymer mods' assets. The shim does not change the hosting configuration or require players to accept the pack. See [Polymer's resource-pack hosting documentation](https://polymer.pb4.eu/latest/user/resource-pack-hosting/) for other hosting setups.

## Build from source

Use **JDK 25** and **Python 3**:

```sh
python3 fetch-dependencies.py
./gradlew build
```

On Windows, use `gradlew.bat build` for the second command. The installable output is `build/libs/mapstitch-polymer-compat-1.0.1+26.3.jar`; the `-sources.jar` is for development.

`dependencies.lock.json` records upstream download URLs and hashes. The fetch script verifies the SHA-512 of each fetched build/runtime JAR and refuses mismatched files. Gradle resolves Minecraft, Fabric, and Polymer build dependencies from their configured repositories. The output contains the shim's own code and resources, including license notices; upstream mod JARs are not bundled.

## Verification and limits

Recorded acceptance testing covers simultaneous vanilla, native MapStitch, and MapStitch-with-Polymer connections; native crafting and atlas insertion/ejection; minimap and world-map behavior; Survival and Creative/operator cases in the Overworld, Nether, and End; and persistence through reload and restart. Resource regression coverage includes **129 assertions** for all five atlas artwork states, accepted/declined/absent pack status, unchanged native stacks, and fallback serialization. Held-item notice checks include both hands and the absence of notices for native clients. See [VALIDATION.md](VALIDATION.md) for the published validation summary.

These are focused acceptance results, not certification of every mod combination. Optional MapStitch accessory and Remapped integrations, custom dimension mods, and other Polymer mods' enhanced client metadata have not been exhaustively tested. Other client-required mods still need their own compatibility solution. Raw game profiles, worlds, logs, and test fixtures are not required to build or run the shim.

## License and credits

The shim is [MIT licensed](LICENSE), copyright 2026 THENATHE. MapStitch is created by pajic; Polymer is a separate upstream project. The upstream atlas artwork remains covered by its original [MIT notice](src/main/resources/licenses/mapstitch-LICENSE.txt), which is included when the artwork is copied into the generated pack. This repository distributes the compatibility layer, not the original mod binaries.
