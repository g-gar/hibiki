# Hibiki Feature Specification

Hibiki is a self-hosted, sovereign music platform designed as an open-source alternative to Spotify and SoundCloud. It focuses on high-fidelity audio, professional metadata management, and a modular ecosystem.

## 1. Media Ingestion & Processing

The ingestion engine is built for storage efficiency and data integrity.

* **Batch Upload Service**: Support for structured album uploads via drag-and-drop.

* **Web-to-Library Ingestion**: Native **yt-dlp** integration to import from YouTube Music, SoundCloud, Bandcamp, and 1000+ other sources.

* **Smart Deduplication**: Global content-aware storage using **SHA-256** fingerprinting to avoid binary duplicates.

* **Agnostic Storage**: Pluggable architecture supporting local filesystems and S3-compatible cloud storage (MinIO, AWS, Backblaze).

## 2. High-Fidelity Streaming

* **On-Demand Streaming**: Low-latency audio delivery with **Gapless Playback** support.

* **Multi-Device Sync**: Real-time synchronization of playback queues and library state across web, mobile, and desktop clients.

* **Lossless Support**: Native handling of FLAC, ALAC, and WAV with dynamic transcoding (Opus/AAC) for bandwidth optimization.

* **Bit-Perfect Output**: Exclusive audio mode (ASIO/WASAPI/PipeWire) for native desktop clients.

## 3. Professional Metadata Management

Hibiki treats music data with professional rigor.

* **MusicBrainz Integration**: Exclusive source of truth for artist biographies, discographies, and credits.

* **Acoustic Fingerprinting**: Automated audio identification via **AcoustID** to correct malformed tags autonomously.

* **Hyper-Filtering Engine**: Granular library exploration using:

  * **Technical Attributes**: BPM, Key, Time Signature.

  * **Contextual Attributes**: Dominant Instruments, Mood, Genre.

* **Synchronized Lyrics**: Support for `.lrc` files and external lyrics providers.

## 4. Discovery, Analytics & Navigation

* **Listener Analytics**: In-depth visualization of listening habits and favorite artists (Integrated Last.fm-style stats).

* **Sovereign Recommendation Engine**: Server-side personalized discovery algorithms that respect user privacy.

* **Hierarchical Browsing**: Advanced navigation by Label, Producer, Composer, and Contributors.

## 5. Interoperability & Open Ecosystem

* **Fediverse Integration (ActivityPub)**: Hibiki accounts act as federated social nodes. Follow artists from Mastodon or Pixelfed directly.

* **Subsonic API Compatibility**: Seamless integration with existing mobile apps (DSub, Amperfy, Play:Sub).

* **Data Portability**: Full export of library and metadata in open formats (JSON/CSV) to ensure digital sovereignty.

## 6. Security & Access Control

* **Granular Visibility**: Authority-based access for tracks and collections (Public, Private, Shared).

* **Hardened Access**: Multi-Factor Authentication (**MFA/TOTP**), active device management, and detailed audit logs.

## 7. Social & Collaborative Ecosystem

* **Sovereign Social Feed**: Real-time activity updates from followed creators within the instance.

* **Collaborative Collections**: Shared playlists and managed libraries with granular permission levels.

* **Radio Mode**: Capability for users to broadcast live audio streams to other instance members.

## 8. Extended Content Support

* **Podcast & Audiobook Engine**: RSS feed management, playback progress tracking, and chapter support.

* **Sample Library**: Dedicated space for producers with waveform previews and loop metadata.

## 9. Monetization & Resource Management

* **User Quotas**: System-level limits on storage (GB) and daily bandwidth usage.

* **Subscription Tiers**: Admin-defined tiers (Basic, Pro, Artist) to restrict premium plugins or high-fidelity formats.

## 10. Plugin & Extension Ecosystem (Modular)

Hibiki's core remains lean, while specialized features are injected via a high-performance plugin API.

* **Plugin: Hibiki Live DJ & Virtual Deck**:

  * **WebAudio Mixing**: Two-deck architecture with crossfader and 3-band EQ running on **AudioWorklets** for zero latency.

  * **Hardware Mapping (QMK/VIA Style)**: Visual interface to map Web MIDI/HID controllers or mechanical keyboards to DJ functions.

  * **WebGL Waveforms**: High-performance real-time waveform rendering with Beatgrid detection.

* **Plugin: Dynamic Jam Layering**: Real-time mixing of backing tracks with multiple improvisation layers.

* **Plugin: Personal Music Vault**: Encrypted private space for demos and "Work in Progress" recordings.

* **Plugin: Biometric Sync**: Adaptive BPM adjustment based on real-time health data (Garmin/Fitbit).

* **Plugin: Audio Processing (VST/WASM)**: Server-side or client-side VST effect loading (Compression, EQ, Limiting).

