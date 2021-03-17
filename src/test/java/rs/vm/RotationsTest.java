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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RotationsTest extends RsDialectCoherencyTestBase
{
    @ArgumentsSource(RotateArea.class)
    @DisplayName("Rotate the area and draw an oval.")
    @ParameterizedTest
    void testRotateArea(String code)
    {
        Graphics2D g2dRotate = mock(Graphics2D.class);
        when(g2d.create()).thenReturn(g2dRotate);

        RsVmWithCommand ctx = createRsVm(code);
        ctx.rsVm.visit(ctx.command);

        verify(g2d, times(1)).create();
        verify(g2dRotate, times(1)).rotate(eq(Math.toRadians(91)), eq(100.0d), eq(200.0d));
        verify(g2dRotate, times(1)).setColor(eq(Color.RED));
        verify(g2dRotate, times(1)).setStroke(eq(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
        verify(g2dRotate, times(1)).fill(arcCaptor.capture());

        assertEquals(100 - 30, arcCaptor.getValue().x);
        assertEquals(200 - 70, arcCaptor.getValue().y);
        assertEquals(60, arcCaptor.getValue().width);
        assertEquals(140, arcCaptor.getValue().height);
        assertEquals(0, arcCaptor.getValue().start);
        assertEquals(360, arcCaptor.getValue().extent);
    }

    @ArgumentsSource(RotateOvalInline.class)
    @DisplayName("Rotate an oval.")
    @ParameterizedTest
    void testRotateOvalInline(String code)
    {
        Graphics2D g2dRotate = mock(Graphics2D.class);
        when(g2d.create()).thenReturn(g2dRotate);

        RsVmWithCommand ctx = createRsVm(code);
        ctx.rsVm.visit(ctx.command);

        verify(g2d, times(1)).create();
        verify(g2d, times(1)).setColor(eq(Color.RED));
        verify(g2d, times(1)).setStroke(eq(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
        verify(g2dRotate, times(1)).rotate(eq(Math.toRadians(87)), eq(100.0d), eq(200.0d));
        verify(g2dRotate, times(1)).fill(arcCaptor.capture());

        assertEquals(100 - 30, arcCaptor.getValue().x);
        assertEquals(200 - 70, arcCaptor.getValue().y);
        assertEquals(60, arcCaptor.getValue().width);
        assertEquals(140, arcCaptor.getValue().height);
        assertEquals(0, arcCaptor.getValue().start);
        assertEquals(360, arcCaptor.getValue().extent);
    }

    private static class RotateArea extends CodeProviderBase
    {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
        {
            switch (currentDialect())
            {
            case "ru":
                return Stream.of(
                        of("повернуть область (91) ( рисовать (красный) (овалы ( ((100, 200) 30 на 70) ) ) )"));
            case "en":
                return Stream.of(of("rotate area (91) ( draw (red) (ovals ( ((100, 200) 30 x 70) ) ) )"));
            default:
                throw new UnsupportedOperationException();
            }
        }
    }

    private static class RotateOvalInline extends CodeProviderBase
    {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception
        {
            switch (currentDialect())
            {
            case "ru":
                return Stream.of(of("рисовать (красный) (овалы ( ((100, 200) 30 на 70 повернуть 87) ) )"));
            case "en":
                return Stream.of(of("draw (red) (ovals ( ((100, 200) 30 x 70 rotate 87) ) )"));
            default:
                throw new UnsupportedOperationException();
            }
        }
    }
}
