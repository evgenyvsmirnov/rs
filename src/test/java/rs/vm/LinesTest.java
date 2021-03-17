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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LinesTest extends RsDialectCoherencyTestBase
{
    @ArgumentsSource(DrawLinesFill.class)
    @DisplayName("Specify color, draw and fill a polygon.")
    @ParameterizedTest
    void testFillPolygon(String code)
    {
        RsVmWithCommand ctx = createRsVm(code);
        ctx.rsVm.visit(ctx.command);

        verify(g2d, times(1)).setColor(eq(Color.RED));
        verify(g2d, times(1)).setStroke(eq(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
        verify(g2d, times(1)).fill(polygonCaptor.capture());

        assertArrayEquals(new int[] {100, 200}, polygonCaptor.getValue().xpoints);
        assertArrayEquals(new int[] {101, 201}, polygonCaptor.getValue().ypoints);
    }

    @ArgumentsSource(DrawLinesOutline.class)
    @DisplayName("Specify color, outline a polygon.")
    @ParameterizedTest
    void testOutlinePolygon(String code)
    {
        RsVmWithCommand ctx = createRsVm(code);
        ctx.rsVm.visit(ctx.command);

        verify(g2d, times(1)).setColor(eq(Color.GREEN));
        verify(g2d, times(1)).setStroke(eq(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
        verify(g2d, times(1)).draw(polygonCaptor.capture());

        assertArrayEquals(new int[] {100, 200}, polygonCaptor.getValue().xpoints);
        assertArrayEquals(new int[] {101, 201}, polygonCaptor.getValue().ypoints);
    }

    @ArgumentsSource(DrawLinesRotate.class)
    @DisplayName("Rotate a polygon.")
    @ParameterizedTest
    void testRotatePolygon(String code)
    {
        Graphics2D g2dRotate = mock(Graphics2D.class);
        when(g2d.create()).thenReturn(g2dRotate);

        RsVmWithCommand ctx = createRsVm(code);
        ctx.rsVm.visit(ctx.command);

        verify(g2d, times(1)).setColor(eq(Color.GREEN));
        verify(g2d, times(1)).setStroke(eq(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));

        verify(g2d, times(1)).create();
        verify(g2dRotate, times(1)).rotate(Math.toRadians(110), 150, 160);
        verify(g2dRotate, times(1)).draw(polygonCaptor.capture());
        verify(g2dRotate, times(1)).dispose();

        assertArrayEquals(new int[] {100, 200}, polygonCaptor.getValue().xpoints);
        assertArrayEquals(new int[] {110, 210}, polygonCaptor.getValue().ypoints);
    }

    private static class DrawLinesFill extends CodeProviderBase
    {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
        {
            switch (currentDialect())
            {
            case "ru":
                return Stream.of(of("рисовать (красный) (линии ((100, 101) (200, 201)))"));
            case "en":
                return Stream.of(of("draw (red) (lines ((100, 101) (200, 201)))"));
            default:
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class DrawLinesOutline extends CodeProviderBase
    {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
        {
            switch (currentDialect())
            {
            case "ru":
                return Stream.of(of("рисовать (зеленый, 2) (линии ((100, 101) (200, 201)))"));
            case "en":
                return Stream.of(of("draw (green, 2) (lines ((100, 101) (200, 201)))"));
            default:
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class DrawLinesRotate extends CodeProviderBase
    {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
        {
            switch (currentDialect())
            {
            case "ru":
                return Stream.of(of("рисовать (зеленый, 2) (линии ((100, 110) (200, 210) повернуть 110))"));
            case "en":
                return Stream.of(of("draw (green, 2) (lines ((100, 110) (200, 210) rotate 110))"));
            default:
                throw new UnsupportedOperationException();
            }
        }
    }
}