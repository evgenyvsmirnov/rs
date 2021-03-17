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
package rs;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.TokenStream;

import rs.vm.RSLexer;
import rs.vm.RSParser;
import rs.vm.RsVm;

public final class RsVmLauncher
{
    public static void main(String[] args)
    {
        String file = (args.length == 0 ? "program.rs" : args[0]);
        if (!Files.exists(Paths.get(file)))
            throw new IllegalArgumentException("File " + file + " not found.");

        EventQueue.invokeLater(() -> {
            RsVmScreen screen = new RsVmScreen(g2d -> {
                try
                {
                    InputStream srcStream = Files.newInputStream(Paths.get(file));
                    Lexer lexer = new RSLexer(CharStreams.fromStream(srcStream));
                    TokenStream tokenStream = new CommonTokenStream(lexer);
                    RSParser parser = new RSParser(tokenStream);
                    // Set up an error handler
                    RSParser.CommandContext command = parser.command();

                    RsVm rsVm = new RsVm(g2d);
                    rsVm.visit(command);
                }
                catch (IOException e)
                {
                    throw new UncheckedIOException("Failed to read " + file, e);
                }
            });
            screen.setVisible(true);
        });
    }

    private RsVmLauncher()
    {
    }

    static class RsVmScreen extends JFrame
    {
        private static final String TITLE = "RS";

        private final Consumer<Graphics2D> vm;

        RsVmScreen(Consumer<Graphics2D> vm)
        {
            this.vm = vm;
        }

        @Override
        public void setVisible(boolean b)
        {
            add(new RsVmPanel(vm));

            setTitle(TITLE);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);

            super.setVisible(b);
        }
    }

    private static class RsVmPanel extends JPanel
    {
        private final Consumer<Graphics2D> vm;

        RsVmPanel(Consumer<Graphics2D> vm)
        {
            this.vm = vm;
        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            doDrawing(g);
        }

        private void doDrawing(Graphics g)
        {
            vm.accept((Graphics2D)g);
        }
    }
}