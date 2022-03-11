# Use Arch Linux as base image
FROM archlinux
WORKDIR /root/docker
# Upgrade packages
RUN pacman -Syuu --noconfirm
# Install python
RUN pacman -S --noconfirm python