import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:harpy/core/misc/harpy_theme.dart';
import 'package:harpy/core/shared_preferences/theme/harpy_theme_data.dart';
import 'package:harpy/models/settings_model.dart';
import 'package:harpy/models/theme_model.dart';
import 'package:scoped_model/scoped_model.dart';

/// The model for creating or editing a custom theme.
class CustomThemeModel extends Model {
  CustomThemeModel({
    @required this.themeModel,
    @required this.settingsModel,
  })  : assert(themeModel != null),
        assert(settingsModel != null) {
    // initialize the custom theme data with the edited theme or the current
    // theme when creating a new theme
    customThemeData = HarpyThemeData()..fromTheme(themeModel.harpyTheme);
    customThemeData.name = "New theme 1";
  }

  final ThemeModel themeModel;
  final SettingsModel settingsModel;

  static CustomThemeModel of(BuildContext context) {
    return ScopedModel.of<CustomThemeModel>(context);
  }

  HarpyThemeData customThemeData;

  /// `true` if the name only contains valid characters.
  bool validName = true;

  /// Returns the error text if an error exists, otherwise `null`.
  String errorText() {
    if (customThemeData.name?.isEmpty ?? true) {
      return null;
    }

    if (!validName) {
      return "Name contains invalid characters";
    }

    return null;
  }

  HarpyTheme get harpyTheme => HarpyTheme.custom(customThemeData);

  int get initialTabControllerIndex => customThemeData.base == "light" ? 0 : 1;

  void changeName(String name) {
    customThemeData.name = name;

    // validate name
    validName = _validateName();
  }

  void changeBase(int index) {
    customThemeData.base = index == 0 ? "light" : "dark";
    notifyListeners();
  }

  void changeAccentColor(Color color) {
    customThemeData.accentColor = color.value;
    notifyListeners();
  }

  void changePrimaryBackgroundColor(Color color) {
    customThemeData.primaryBackgroundColor = color.value;
    notifyListeners();
  }

  void changeSecondaryBackgroundColor(Color color) {
    customThemeData.secondaryBackgroundColor = color.value;
    notifyListeners();
  }

  void changeLikeColor(Color color) {
    customThemeData.likeColor = color.value;
    notifyListeners();
  }

  void changeRetweetColor(Color color) {
    customThemeData.retweetColor = color.value;
    notifyListeners();
  }

  /// Returns `true` if the name only contains alphanumeric characters, '-', '_'
  /// and spaces.
  bool _validateName() {
    return customThemeData.name.contains(RegExp(r"^[-_ a-zA-Z0-9]+$"));
  }
}
