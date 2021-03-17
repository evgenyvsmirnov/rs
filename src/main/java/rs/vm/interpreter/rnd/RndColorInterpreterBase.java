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
package rs.vm.interpreter.rnd;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import rs.vm.CodeBlockResults.ColorResult;
import rs.vm.DrawFrames;
import rs.vm.RSParser.ColorContext;
import rs.vm.RSParser.OfSetContext;
import rs.vm.interpreter.InterpreterBase;

abstract class RndColorInterpreterBase extends InterpreterBase
{
    protected final ColorResult interpretInternal(OfSetContext ctx, DrawFrames frames)
    {
        List<Color> colorsToChoose = new ArrayList<>(ctx.color().size());
        for (ColorContext colorToChoose : ctx.color())
            colorsToChoose.add(mapColor(colorToChoose));

        int rnd = frames.random(colorsToChoose.size());
        return ColorResult.create(colorsToChoose.get(rnd));
    }
}
