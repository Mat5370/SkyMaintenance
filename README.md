# 🔧 SkyMaintenance

**SkyMaintenance** est un plugin léger, efficace et entièrement personnalisable développé pour les serveurs Minecraft utilisant Paper/Spigot 1.21+.  
Il permet d’activer un mode maintenance sur le serveur, avec un système de timer, de permissions et de messages configurables.

> ✨ Conçu initialement pour le serveur [SkyBuild](https://github.com/Mat5370), ce plugin est maintenant accessible publiquement.

---

## 📦 Fonctionnalités principales

- ✅ Activation/désactivation de la maintenance avec `/maintenance on|off`
- ⏳ Support d'une durée personnalisée (`/maintenance on 5m`, `30s`, etc.)
- 👥 Expulsion automatique des joueurs non autorisés
- 🔐 Gestion fine des permissions (`toggle`, `status`, `whitelist`)
- 📝 Personnalisation complète des messages via `config.yml`
- 🗂️ Génération automatique des fichiers `readme.txt` et `logs.txt`
- 🧾 Suivi des actions dans `logs.txt`
- 💬 Messages différenciés pour le staff et les autres joueurs

---

## 🔧 Commandes

```bash
/maintenance on [durée]   # Active la maintenance, optionnellement avec un timer
/maintenance off          # Désactive la maintenance
/maintenance status       # Affiche le statut actuel
```

🔐 Permissions
```bash
Permission	Description
maintenance.toggle	Requise pour activer/désactiver
maintenance.status	Voir l'état de la maintenance
maintenance.whitelist	Exempte de l’expulsion automatique
```

🔄 Installation
1. Téléchargez la dernière version depuis la section Releases
2. Déposez le .jar dans le dossier /plugins de votre serveur
3. Redémarrez le serveur
4. Modifiez config.yml si nécessaire pour personnaliser les messages
5. (Optionnel) Utilisez /reload ou redémarrez pour appliquer les modifications

🧪 Compatibilité
✅ Minecraft : 1.21.x (développé sur Paper 1.21.4)

⚠️ Support potentiel 1.20.6+ (non garanti)

❌ Versions antérieures non supportées officiellement

🧑‍💻 Développement
Développé avec ❤️ par Mat5370
→ Pull requests, suggestions et issues bienvenues !
