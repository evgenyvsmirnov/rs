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

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import rs.vm.RSParser.CommandContext;

public class RsDialectCoherencyTestBase
{
    @Mock protected Graphics2D g2d;

    @Captor protected ArgumentCaptor<Polygon> polygonCaptor;
    @Captor protected ArgumentCaptor<Ellipse2D.Double> ellipseCaptor;
    @Captor protected ArgumentCaptor<Arc2D.Double> arcCaptor;

    protected RsVmWithCommand createRsVm(String code)
    {
        try
        {
            InputStream srcStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
            Lexer lexer = new RSLexer(CharStreams.fromStream(srcStream));
            TokenStream tokenStream = new CommonTokenStream(lexer);
            RSParser parser = new RSParser(tokenStream);
            CommandContext command = parser.command();

            return new RsVmWithCommand(new RsVm(g2d), command);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    protected static final class RsVmWithCommand
    {
        final RsVm rsVm;
        final CommandContext command;

        RsVmWithCommand(RsVm rsVm, CommandContext command)
        {
            this.rsVm = rsVm;
            this.command = command;
        }
    }
}
