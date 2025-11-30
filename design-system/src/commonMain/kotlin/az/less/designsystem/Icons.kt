package az.less.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import lessmobile.design_system.generated.resources.Res
import lessmobile.design_system.generated.resources.ic_arrow_down
import lessmobile.design_system.generated.resources.ic_back_24dp
import lessmobile.design_system.generated.resources.ic_check_success
import lessmobile.design_system.generated.resources.ic_clear_rounded_24dp
import lessmobile.design_system.generated.resources.ic_delete_24dp
import lessmobile.design_system.generated.resources.ic_selected_radio
import lessmobile.design_system.generated.resources.ic_un_selected_radio
import org.jetbrains.compose.resources.painterResource

object DsIcons {

    val ArrowDown: Painter
        @Composable get() = painterResource(Res.drawable.ic_arrow_down)

    val RadioSelected: Painter
        @Composable get() = painterResource(Res.drawable.ic_selected_radio)

    val RadioUnselected: Painter
        @Composable get() = painterResource(Res.drawable.ic_un_selected_radio)

    val Back: Painter
        @Composable get() = painterResource(Res.drawable.ic_back_24dp)

    val Clear: Painter
        @Composable get() = painterResource(Res.drawable.ic_clear_rounded_24dp)

    val Delete: Painter
        @Composable get() = painterResource(Res.drawable.ic_delete_24dp)

    val CheckSuccess: Painter
        @Composable get() = painterResource(Res.drawable.ic_check_success)

}
