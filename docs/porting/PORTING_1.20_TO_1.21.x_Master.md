# Growthcraft Migration Guide
## Minecraft 1.20.x → 1.21.x (NeoForge)

This document defines authoritative migration policy.
AI agents MUST conform to the rules in this document.

---

## 1️⃣ General Migration Philosophy
- Prefer stable APIs
- Avoid deprecated constructors
- Favor explicit over implicit behavior

---

## 2️⃣ Required Global Code Changes
### ResourceLocation Changes
- OLD: new ResourceLocation(...)
- NEW: ResourceLocation.parse(...) or factory methods

Do NOT use deprecated forms.

---

## 3️⃣ Registry Guidance
(we’ll fill this later)

---

## 4️⃣ Event Bus Guidance
(we’ll fill this later)

---

## 5️⃣ Networking Changes
(we’ll fill this later)

---

## 6️⃣ Block Entity Rules
(we’ll fill this later)

---

## 7️⃣ Validation & Testing
- Must compile
- Must load
- Must smoke-run without crash

---

## 8️⃣ Explicit Non-Goals
- Do NOT refactor unrelated logic
- Do NOT introduce modernization changes
- Port only to functional parity

---
