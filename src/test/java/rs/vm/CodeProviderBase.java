// Copyright © 2021 RS Contributors.
//
// Licensed under the Eclipse Public License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. You may
// obtain a copy of the License at https://www.eclipse.org/legal/epl-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//
// See the License for the specific language governing permissions and
// limitations under the License.
package rs.vm;

import org.junit.jupiter.params.provider.ArgumentsProvider;

import static org.junit.jupiter.api.Assertions.fail;

abstract class CodeProviderBase implements ArgumentsProvider
{
    protected final String currentDialect()
    {
        String whiteToken = RSLexer.VOCABULARY.getDisplayName(RSParser.WHITE);
        if ("'белый'".equals(whiteToken))
            return "ru";
        if ("'white'".equals(whiteToken))
            return "en";

        fail("Tests for a language where word \"" + whiteToken + "\" exists haven't been written yet.");

        throw new UnsupportedOperationException(
                "Tests for a language where word \"" + whiteToken + "\" exists haven't been written yet.");
    }
}
