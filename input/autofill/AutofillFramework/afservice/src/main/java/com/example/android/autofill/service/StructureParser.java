/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.autofill.service;

import android.app.assist.AssistStructure;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import static android.app.assist.AssistStructure.ViewNode;

/**
 * Wrapper for {@link AssistStructure} to make it easy to parse.
 */
public final class StructureParser {
    private final AssistStructure mStructure;

    public StructureParser(@NonNull AssistStructure structure) {
        Preconditions.checkNotNull(structure);
        mStructure = structure;
    }

    /**
     * Traverses through the {@link AssistStructure} and does something at each {@link ViewNode}.
     *
     * @param processor contains action to be performed on each {@link ViewNode}.
     */
    public void parse(NodeProcessor processor) {
        int nodes = mStructure.getWindowNodeCount();
        for (int i = 0; i < nodes; i++) {
            AssistStructure.ViewNode viewNode = mStructure.getWindowNodeAt(i).getRootViewNode();
            traverseRoot(viewNode, processor);
        }
    }

    private void traverseRoot(AssistStructure.ViewNode viewNode, NodeProcessor processor) {
        processor.processNode(viewNode);
        int childrenSize = viewNode.getChildCount();
        if (childrenSize > 0) {
            for (int i = 0; i < childrenSize; i++) {
                traverseRoot(viewNode.getChildAt(i), processor);
            }
        }
    }

    public interface NodeProcessor {
        void processNode(ViewNode node);
    }
}
