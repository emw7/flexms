#!/bin/bash

if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <source_directory> <project_name> <resource_name>"
    exit 1
fi

SOURCE_DIR=$1
PROJECT_NAME=$2
RESOURCE_NAME=$3
TEMP_DIR=$(mktemp -d)

# Copy source directory to temporary directory
cp -r "$SOURCE_DIR" "$TEMP_DIR/source"
cd "$TEMP_DIR/source" || exit 1

# Rename directories and files
find . -depth -name "*@@PROJECT_NAME@@*" -or -name "*@@RESOURCE_NAME@@*" | while read -r file; do
    new_name=$(echo "$file" | sed "s/@@PROJECT_NAME@@/$PROJECT_NAME/g" | sed "s/@@RESOURCE_NAME@@/$RESOURCE_NAME/g")
    mv "$file" "$new_name"
done

# Replace contents inside files
find . -type f -exec sed -i "s/@@PROJECT_NAME@@/$PROJECT_NAME/g" {} +
find . -type f -exec sed -i "s/@@RESOURCE_NAME@@/$RESOURCE_NAME/g" {} +

# Create the tar.gz archive
cd "$TEMP_DIR" || exit 1
tar czf "${PROJECT_NAME}_${RESOURCE_NAME}.tgz" source

# Move the archive to the original location
mv "${PROJECT_NAME}_${RESOURCE_NAME}.tgz" "$PWD"

# Cleanup
tmp_dir=$(dirname "$TEMP_DIR")
rm -rf "$TEMP_DIR"

echo "Archive ${PROJECT_NAME}_${RESOURCE_NAME}.tgz created successfully."
