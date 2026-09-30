# Release validation

## Published artifact

MapStitch Polymer Compatibility **1.0.1+26.3** is the installable artifact for the MapStitch Polymer Shim repository. Its code and release JAR were unchanged during the 2026-09-30 Shared Region Maps compatibility update.

```text
ce6a6a1581bc5531bc9c0d8ec653ef48f993265bc7769e177cfc2933aeaa3d27  mapstitch-polymer-compat-1.0.1+26.3.jar
```

The tested platform is Minecraft 26.3, Fabric Loader 0.19.5, Java 25, Fabric API 0.161.0+26.3, Fzzy Config 0.7.7+fix2+26.3, and Fabric Language Kotlin 1.14.1+kotlin.2.4.20.

## September 30: Shared Region Maps 1.0.3 interoperability

| MapStitch | Polymer + shim | Metadata checks | Sharing checks |
| --- | --- | ---: | ---: |
| Original 1.1.6+26.3 | Installed | 1,623 | 579 |
| Original 1.1.6+26.3 | Absent control | 1,623 | 579 |
| Toolpouch-patch.1 | Installed | 1,623 | 579 |
| Toolpouch-patch.1 | Absent control | 1,623 | 579 |

All **8,808 assertions passed**, all eight servers saved and exited normally, and dependency/artifact hashes remained unchanged. Polymer was Bundled 0.18.2+26.3. The patched MapStitch + Polymer sharing run also loaded Map Atlases port.5, Moonlight port.3, CodecUI port.1, and Accurate Maps port.2.

Checks exercised all five scales and three built-in dimensions: automatic atlas generation for two simulated players, ordinary-first and atlas-first creation, canonical IDs and pixels, insertion/ejection, shared and independent zooming, missing/stale map centers, and existing-atlas repair. The absent controls establish that Shared Region Maps can work without Polymer or this shim; they are not shim runtime tests.

These checks use dedicated-server engine calls. They do not establish new live-client rendering/network coverage or Tool Pouch gameplay coverage. Known nonfatal Moonlight empty-registry diagnostics occurred in the combined stack.

## September 25: shim 1.0.1 acceptance

The recorded release tests included:

- A **129-assertion resource regression** covering all five atlas fullness states with absent, accepted, and declined pack status; fallback item-data roundtrip; native stack preservation; and unchanged source components.
- Generated resource-pack inspection: byte-identical sprites from the installed upstream MapStitch JAR, vanilla item definitions, and the original MIT artwork notice.
- Both native client profiles passing the **12-case dimension/mode matrix**: MapStitch alone and MapStitch + Polymer, with real inventory operations, Creative item-slot packets, and operator commands.
- An actual unmodified client joining concurrently and displaying all five atlas sprites with the optional pack accepted.
- Final artwork acceptance checks after adding the upstream license notice, and an unmodified client explicitly declining the optional pack while retaining the book fallback and connection.

The earlier 1.0.0 acceptance record also covered vanilla/native mixed connections, reload/reconnect/full-server-restart inventory preservation, and observation of the held-atlas action-bar notice. Those historical checks are not presented as fresh tests of every subsequent release.

## Scope

Vanilla clients can join and retain an intact fallback atlas; the shim does not implement MapStitch’s client-side atlas GUI, minimap, or world map for them. Native clients receive their original atlas data. Enhanced Polymer item/component registry caches are suppressed on native MapStitch + Polymer connections; enhanced metadata/virtual items from unrelated Polymer mods on those connections remain outside the supported scope.

Optional accessory/Remapped integrations, custom dimensions, all third-party combinations, and every MapStitch UI option are not certified. Upstream dependency URLs and checksums are recorded in `dependencies.lock.json`. Raw worlds, screenshots, session data, and machine-specific fixture logs remain local and are excluded from this repository.
