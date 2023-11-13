#!/bin/bash

git -C $1 reflog HEAD
git -C $1 rev-parse HEAD
